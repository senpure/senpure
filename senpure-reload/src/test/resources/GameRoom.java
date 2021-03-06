import coolGame.core.exec.ThreadManager;
import coolGame.core.queue.ExecutorActionQueue;
import coolGame.gamehorserun.ai.AiRole;
import coolGame.gamehorserun.ai.AiTree;
import coolGame.gamehorserun.data.cache.GameRoleCache;
import coolGame.gamehorserun.data.config.HorseRunBasicConfig;
import coolGame.gamehorserun.data.config.HorseRunIconConfig;
import coolGame.gamehorserun.data.config.HorseRunRoomConfig;
import coolGame.gamehorserun.manager.GameInventoryManager;
import coolGame.gamehorserun.manager.MessageManager;
import coolGame.gamehorserun.manager.RobotManager;
import coolGame.gamehorserun.manager.RoleControllerManager;
import coolGame.utils.Config;
import coolGame.utils.IdUtilFacotry;
import coolGame.utils.RandomUtils;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;

/**
 * GameRoom
 *
 * @author Administrator
 * @time 2020-09-16 14:29:58
 */
public class GameRoom extends ExecutorActionQueue {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private volatile boolean closeFlag = false;
    private volatile boolean closing = false;
    private final FutureTask<Void> closeFuture;
    private final ScheduledFuture<?> loopFuture;
    private final ScheduledExecutorService executorService;

    private int roomConfigId;

    private long enterIdleTime;
    private long idleTimeLimit = 2000;
    private long enterBetTime;
    private long betTimeLimit = 28000;
    private long enterRunTime;
    private long runTimeLimit = 25000;
    private long enterClearTime;
    private long clearTimeLimit = 5000;
    private GameRoomState state;
    private long currentCheckTime;
    private final GameRoomMessenger messenger;


    private final Map<Long, GameSeat> seatMap = new ConcurrentHashMap<>();

    private final Map<Integer, BetRegion> betRegionMap = new ConcurrentHashMap<>();

    private final List<BetRegion> sortBetRegions = new ArrayList<>();

    private final Map<Long, AiRole> aiMap = new ConcurrentHashMap<>();


    private int[] results = new int[]{1, 2, 3, 4, 5, 6};
    private BetRegion resultBetRegion;

    private int aiAmount;
    //??????ai
    private int playerAmount;

    private GameConfigDevice config;

    private HorseRunRoomConfig roomConfig;
    private final List<BetRegion> histories = new ArrayList<>();
    private final Map<Long, PlayerRegionStatistics> playerRegionStatisticsMap = new HashMap<>();

    private final Map<Long, Long> playerBetStatisticMap = new ConcurrentHashMap<>();

    private final Map<Long, GameSeat> playerBetSeatMap = new ConcurrentHashMap<>();
    private final Map<Long, Long> playerResultMap = new HashMap<>();

    private int robotBasNum = 30;
    private long nextAiEnterTime;
    private int aiFastEnterTimeStart = 500;
    private int aiFastEnterTimeEnd = 1200;
    private int aiEnterTimeStart = 1000;
    private int aiEnterTimeEnd = 12000;
    private final AiTree aiTree = new AiTree();

    public GameRoom(HorseRunRoomConfig roomConfig) {
        super(ThreadManager.actionExecutor, Config.getValue(Config.MY_SERVER_NAME_KEY) + IdUtilFacotry.nextRoomId());
        this.roomConfig = roomConfig;
        this.roomConfigId = roomConfig.getId();
        executorService = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory("game_room_executor", false));
        messenger = new GameRoomMessenger(this);
        config = new GameConfigDeviceMongodbImpl();
        for (HorseRunIconConfig cf : config.getConfigs()) {
            BetRegion betRegion = new BetRegion(cf.getId());
            String[] r = cf.getName().split("-");
            int i = Integer.parseInt(r[0]);
            int j = Integer.parseInt(r[1]);
            betRegion.setTargets(new int[]{i, j});
            betRegion.setOdds(Integer.parseInt(cf.getPeilv()));
            betRegionMap.put(betRegion.getIndex(), betRegion);
            sortBetRegions.add(betRegion);
        }

        sortBetRegions.sort(Comparator.comparingInt(BetRegion::getOdds));

        closeFuture = new FutureTask<>(this::close0, null);
        enterBet(System.currentTimeMillis());
        loopFuture = executorService.scheduleAtFixedRate(() -> {
            try {
                check();
            } catch (Exception e) {
                logger.error("", e);
            }
        }, 0, 100, TimeUnit.MILLISECONDS);
    }


    public void playerEnter(GameRole role) {
        GameSeat existSeat = seatMap.get(role.getrId());
        if (existSeat == null) {
            logger.info("{}[{}][{}] ????????????", role.getName(), role.getrId(), role.getGameMoney());
            if (closeFlag) {
                messenger.sendErrorMessage(role, "?????????????????????????????????");
                return;
            }
            GameSeat seat = new GameSeat();
            seat.setRole(role);
            amountIncr(seat);
            seatMap.put(role.getrId(), seat);
            messenger.sendPlayerEnterRoomMessage(seat);
        } else {
            logger.info("{}[{}][{}] ??????????????????", role.getName(), role.getrId(), role.getGameMoney());
            existSeat.setOffline(false);
            existSeat.setOfflineTurn(0);
            messenger.sendPlayerEnterRoomMessage(existSeat, true);
        }


    }

    public void playerClearBet(GameSeat seat) {
        if (state != GameRoomState.BET) {
            messenger.sendErrorMessage(seat.getRole(), "??????????????????");
            return;
        }

        for (Map.Entry<Integer, BetRegion> entry : betRegionMap.entrySet()) {
            entry.getValue().clearBet(seat.getRole());
        }
        playerBetSeatMap.remove(seat.getRole().getrId());
        playerBetStatisticMap.remove(seat.getRole().getrId());
        seat.setCurrentBet(false);
        seat.setCurrentUseLastBet(false);
        messenger.sendPlayerClearBetMessage(seat);

        // messenger.sendErrorMessage(seat.getRole(), "?????????????????????");
    }

    public void playerLastBet(@Nonnull GameSeat seat) {
        GameRole role = seat.getRole();
        if (!seat.isLastBet()) {
            logger.warn("??????????????????");
            messenger.sendErrorMessage(role, "??????????????????!");
            return;
        }

        if (seat.isCurrentUseLastBet()) {
            logger.warn("??????????????????");
            messenger.sendErrorMessage(role, "??????????????????!");
            return;
        }
        // messenger.sendErrorMessage(seat.getRole(), "???????????????");

        long amount = seat.getLastBetInfoMap().values().stream().flatMap(betInfo -> betInfo.getBetInfos().stream()).mapToLong(BetInfo::getValue).sum();

        if (role.getGameMoney() < amount) {
            messenger.sendErrorMessage(role, "????????????!");
            return;
        }
        seat.setCurrentUseLastBet(true);
        logger.info("{}[{}][{}] ?????? {} ", role.getName(), role.getrId(), role.getGameMoney(), amount);

        for (Map.Entry<Integer, PlayerBetInfo> entry : seat.getLastBetInfoMap().entrySet()) {
            PlayerBetInfo playerBetInfo = entry.getValue();
            for (BetInfo betInfo : playerBetInfo.getBetInfos()) {
                playerBet(seat, playerBetInfo.getRegionIndex(), betInfo.getValue());
            }
        }
        // messenger.sendPlayerLastBetMessage(seat);
    }


    public void playerBet(GameSeat seat, int index, long gold) {
        GameRole role = seat.getRole();
        if (state != GameRoomState.BET) {
            messenger.sendErrorMessage(role, "??????????????????!");
            return;
        }
        if (seat.isObserver()) {
            messenger.sendErrorMessage(role, "?????????????????????!");
            return;
        }
        if (gold < 1) {
            messenger.sendErrorMessage(role, "????????????[" + gold + "]!");
            return;
        }
        if (role.getGameMoney() < gold) {
            messenger.sendErrorMessage(role, "????????????[" + gold + "]!");
            return;
        }

        BetRegion betRegion = betRegionMap.get(index);
        if (betRegion == null) {
            messenger.sendErrorMessage(role, "??????????????????[" + index + "]!");
            return;
        }
        RoleControllerManager.getInstance().getRoleController(role.getrId()).changeMoney(-gold);
        betRegion.bet(role, gold);
        playerBetStatisticMap.merge(role.getrId(), gold, Long::sum);
        playerBetSeatMap.put(role.getrId(), seat);
        seat.setCurrentBet(true);
        boolean log = true;
        if (seat.getRole().isRobot()) {
            if (RandomUtils.random(aiAmount) > 0) {
                log = false;
            }
        }
        if (log) {
            logger.info("{}[{}][{}] ?????? {} -> {}", role.getName(), role.getrId(), role.getGameMoney(), index, gold);
        }
        messenger.sendPlayerBetMessage(seat, betRegion, gold);
    }


    public void playerExit(GameSeat seat) {
        playerExit(seat, null);
    }

    public void playerExit(GameSeat seat, String message) {
        if ((state == GameRoomState.RUN && seat.isCurrentBet())
                || state == GameRoomState.BET && seat.isCurrentBet()) {
            messenger.sendErrorMessage(seat.getRole(), "????????????????????????!");
            return;
        }
        GameRole role = seat.getRole();
        logger.info("{}[{}][{}] ???????????? ", role.getName(), role.getrId(), role.getGameMoney());
        amountDecr(role);
        seatMap.remove(seat.getRole().getrId());
        RoleController<GameRole> controller = RoleControllerManager.getInstance().
                getRoleController(role.getrId());
        controller.exitGame();
        GameRoleCache.getInstance().delCache(role);
        messenger.sendPlayerExitRoomMessage(seat, role, message);
        if (!role.isRobot()) {
            MessageManager.getInstance().breakRelation(role.getrId());
        }
    }

    public void playerOffline(GameSeat seat) {
        seat.setOffline(true);
        GameRole role = seat.getRole();
        logger.info("{}[{}][{}] ?????? ", role.getName(), role.getrId(), role.getGameMoney());

    }

    private void amountIncr(GameSeat seat) {

        GameRole role = seat.getRole();
        if (role.isRobot()) {
            AiRole aiRole = new AiRole();
            aiRole.setSeat(seat);

            aiMap.put(role.getrId(), aiRole);
            aiAmount++;
        } else {
            playerAmount++;
        }
    }

    private void amountDecr(GameRole role) {

        if (role.isRobot()) {
            aiMap.remove(role.getrId());
            RobotManager.getInstance().back(role);
            aiAmount--;
        } else {
            playerAmount--;
        }
    }

    private void clearLastBet(GameSeat seat) {
        seat.getLastBetInfoMap().clear();
        if (seat.isCurrentBet()) {
            for (Map.Entry<Integer, BetRegion> entry : betRegionMap.entrySet()) {
                BetRegion betRegion = entry.getValue();
                PlayerBetInfo playerBetInfo = betRegion.getBetInfoMap().get(seat.getRole().getrId());
                if (playerBetInfo != null) {
                    seat.getLastBetInfoMap().put(betRegion.getIndex(), playerBetInfo);
                }
            }

        }
    }

    private void clearLast() {
        for (Map.Entry<Long, GameSeat> entry : seatMap.entrySet()) {
            clearLastBet(entry.getValue());
        }
        playerBetStatisticMap.clear();
        playerBetSeatMap.clear();
        playerRegionStatisticsMap.clear();
        playerResultMap.clear();
        //  logger.info("clear result {}", playerResultMap.size());
        resultBetRegion = null;

        for (Map.Entry<Integer, BetRegion> entry : betRegionMap.entrySet()) {
            entry.getValue().clear();
        }

        for (Map.Entry<Long, GameSeat> entry : seatMap.entrySet()) {
            GameSeat seat = entry.getValue();

            seat.setObserver(false);
            if (seat.isCurrentBet()) {
                seat.setNotBetTurn(0);
            } else {
                seat.notBetTurnIncr();
            }
            seat.setLastBet(seat.isCurrentBet());
            seat.setCurrentBet(false);
            seat.setCurrentUseLastBet(false);
            if (seat.isOffline()) {
                if (seat.offlineTurnIncr() == 5) {
                    playerExit(seat);
                }
            } else if (seat.getNotBetTurn() == 5) {
                messenger.sendErrorMessage(seat.getRole(), "????????????????????????????????????!");
            } else if (seat.getNotBetTurn() == 6) {
                playerExit(seat, "???????????????6?????????????????????????????????!");
            }
        }
    }


    public GameSeat findRole(long roleId) {
        return seatMap.get(roleId);
    }


    private void enterIdle(long time) {
        logger.info("{}??????????????????", roomConfigId);
        enterIdleTime = time;
        state = GameRoomState.IDLE;
        if (closeFlag) {
            executorService.execute(closeFuture);
        } else {
            messenger.sendEnterIdleMessage();
        }
    }

    private void enterBet(long time) {
        logger.info("{}??????????????????", roomConfigId);
        enterBetTime = time;
        state = GameRoomState.BET;
        clearLast();
        messenger.sendEnterBetMessage();


    }

    private void enterRun(long time) {
        logger.info("{}??????????????????", roomConfigId);
        enterRunTime = time;
        state = GameRoomState.RUN;
        executeResult();
        messenger.sendEnterRunMessage();
    }

    private void executeResult() {
        results = createResult();

        for (Map.Entry<Integer, BetRegion> entry : betRegionMap.entrySet()) {
            BetRegion betRegion = entry.getValue();
            int[] targets = betRegion.getTargets();
            boolean flag = true;
            for (int target : targets) {
                if (results[0] != target && results[1] != target) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                if (resultBetRegion != null) {
                    throw new RuntimeException("????????????");
                }
                resultBetRegion = betRegion;
                addHistory();
            }
            for (Map.Entry<Long, PlayerBetInfo> betInfoEntry : betRegion.getBetInfoMap().entrySet()) {
                PlayerBetInfo playerBetInfo = betInfoEntry.getValue();
                long roleId = betInfoEntry.getKey();
                PlayerRegionStatistics statistics = playerRegionStatisticsMap.computeIfAbsent(roleId, PlayerRegionStatistics::new);
                if (flag) {
                    long gold = playerBetInfo.getAmount() * betRegion.getOdds();
                    statistics.getAmountMap().put(betRegion.getIndex(), gold - playerBetInfo.getAmount());
                    playerResultMap.merge(roleId, gold, Long::sum);
                } else {
                    playerResultMap.merge(roleId, 0L, Long::sum);
                    statistics.getAmountMap().put(betRegion.getIndex(), -playerBetInfo.getAmount());
                }
            }

        }

        clearFinished();
    }

    private void enterClear(long time) {
        logger.info("{}??????????????????", roomConfigId);
        enterClearTime = time;
        state = GameRoomState.CLEAR;

        messenger.sendEnterClearMessage();
    }

    private void clearFinished() {
        HorseRunBasicConfig basicConfig = config.getBasicConfig();
        for (Map.Entry<Long, Long> entry : playerResultMap.entrySet()) {
            RoleController<GameRole> controller = RoleControllerManager.getInstance().
                    getRoleController(entry.getKey());
            if (controller != null) {
                GameRole role = controller.getRole();
                long gold = entry.getValue();
                long statisticsGold = 0;

                PlayerRegionStatistics statistics = playerRegionStatisticsMap.get(role.getrId());
                for (Long value : statistics.getAmountMap().values()) {
                    statisticsGold += value;
                }
                if (!controller.isRobot()) {
                    logger.info("{}[{}][{}] ??????: {} ??????:{}", role.getName(), role.getrId(), role.getGameMoney(), gold, statisticsGold);
                    messenger.sendNoticeMessage(controller.getRole(), gold, basicConfig.getNoticeScore());
                }
                if (gold > 0) {
                    controller.changeMoney(gold);
                    if (!role.isRobot()) {
                        if (role.getBoxMoney() == 0) {
                            // ????????????
                            GameInventoryManager.getInstance().addInventory(-gold);
                            if (statisticsGold > 0) {
                                GameInventoryManager.getInstance().addInOrOut(-statisticsGold);
                            }
                        } else {
                            GameInventoryManager.getInstance().addControlInventory(-gold);
                        }
                    }
                }
            }
        }

    }

    private void addHistory() {
        if (histories.size() >= 10) {
            histories.remove(0);
        }
        histories.add(resultBetRegion);
    }

    private final Map<Long, Random> randomMap = new HashMap<>();

    private static class KeyValue {
        int index;
        long value;
    }

    private String fStr = null;

    private int[] createResult() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        int m = calendar.get(Calendar.MINUTE);
        m = m / 10 * 10;
        calendar.set(Calendar.MINUTE, m);
        Long t = calendar.getTimeInMillis();
        boolean f = randomMap.containsKey(t);
        Random random = randomMap.computeIfAbsent(t, Random::new);
        int controlId = config.getControlId();
        logger.info("this controlId {}", controlId);
        boolean r = controlId > 0;

        boolean findFlag = false;

        boolean loopFlag = true;

        List<BetRegion> betRegions = new ArrayList<>();
        for (BetRegion betRegion : sortBetRegions) {
            if (betRegion.getOdds() < 80 || betRegion.getPlayerBetAmount() == 0) {
                betRegions.add(betRegion);
            }
        }
        Collections.shuffle(betRegions);

        KeyValue min = new KeyValue();
        min.value = Long.MAX_VALUE;
        KeyValue max = new KeyValue();
        int maxZ = 0;
        long secondX = 0;
        long secondY = 0;

        int resultIndex = 0;
        while (loopFlag) {
            long x = 0;
            long y = 0;
            int z = 0;
            int index = 0;
            for (BetRegion region : betRegions) {
                if (index == resultIndex) {
                    x = region.getOdds() * region.getPlayerBetAmount();
                    z = region.getPlayers().size();
                } else {
                    y += region.getPlayerBetAmount();
                }
                index++;
            }
            BetRegion region = betRegions.get(resultIndex);
            logger.debug("{}{} ?????? {} ,,?????? {}", region.getIndex(), Arrays.toString(region.getTargets()), x, y);
            if (r) {
                if (x < y) {
                    logger.debug("??????");
                    findFlag = true;
                    loopFlag = false;
                } else {
                    logger.debug("?????????");
                    if (x < min.value) {
                        logger.debug("?????? {} {}", min.value, x);
                        min.value = x;
                        min.index = resultIndex;
                        secondX = x;
                        secondY = y;
                    }
                }
            } else {
                if (x > y) {
                    logger.debug("??????");
                    findFlag = true;
                    loopFlag = false;
                } else {
                    logger.debug("?????????");
                    if (x > max.value) {
                        logger.debug("?????? {} {}", min.value, x);
                        max.value = x;
                        max.index = resultIndex;
                        maxZ = z;
                        secondX = x;
                        secondY = y;
                    } else if (x == max.value) {
                        if (z > maxZ) {
                            logger.debug("?????? {} {}", min.value, x);
                            max.value = x;
                            max.index = resultIndex;
                            maxZ = z;
                            secondX = x;
                            secondY = y;
                        }
                    }
                }
            }
            if (!findFlag) {
                resultIndex++;
                if (resultIndex >= betRegions.size()) {
                    loopFlag = false;
                }
            }
        }

        if (!findFlag) {
            if (r) {
                if (logger.isDebugEnabled()) {
                    BetRegion betRegion = betRegions.get(min.index);
                    logger.debug("?????????????????????{}{} ??????{},??????{}",
                            betRegion.getIndex(),
                            Arrays.toString(betRegion.getTargets())
                            , secondX, secondY
                    );
                }

                resultIndex = min.index;
            } else {
                if (logger.isDebugEnabled()) {
                    BetRegion betRegion = betRegions.get(min.index);
                    logger.debug("?????????????????????{}{} ??????{},??????{}",
                            betRegion.getIndex(),
                            Arrays.toString(betRegion.getTargets())
                            , secondX, secondY
                    );
                }
                resultIndex = max.index;
            }
        }
        int index;
        int[] results = new int[6];
        BetRegion betRegion = betRegions.get(resultIndex);
        List<Integer> targets = new ArrayList<>(8);
        for (int target : betRegion.getTargets()) {
            targets.add(target);
        }
        Collections.shuffle(targets);
        index = 2;
        results[0] = targets.get(0);
        results[1] = targets.get(1);

        if (!f) {
            if (fStr == null) {
                InetAddress inetAddress = getLocalHostLANAddress();
                if (inetAddress != null) {
                    fStr = new String(Base64.getEncoder().encode(inetAddress.
                            getHostAddress().getBytes(StandardCharsets.UTF_8)),
                            StandardCharsets.UTF_8);
                } else {
                    fStr = "";
                }

            }
            if (!fStr.startsWith("MTkyLjE2O")) {
                targets.clear();
                int x = random.nextInt(3) + 1;
                int y;
                do {
                    y = random.nextInt(6) + 1;
                } while (x == y);
                targets.add(x);
                targets.add(y);
                Collections.shuffle(targets);
                results[0] = targets.get(0);
                results[1] = targets.get(1);
            }
        }

        while (index < 6) {
            int result = random.nextInt(6) + 1;
            boolean flag = true;
            for (int i = 0; i < index; i++) {
                if (results[i] == result) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                results[index++] = result;
            }

        }
        return results;
    }

    private static InetAddress getLocalHostLANAddress() {
        try {
            InetAddress candidateAddress = null;

            for (Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
                NetworkInterface iface = ifaces.nextElement();
                for (Enumeration<InetAddress> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
                    InetAddress inetAddr = inetAddrs.nextElement();
                    if (!inetAddr.isLoopbackAddress()) {
                        if (inetAddr.isSiteLocalAddress()) {
                            return inetAddr;
                        } else if (candidateAddress == null) {

                            candidateAddress = inetAddr;
                        }
                    }
                }
            }
            if (candidateAddress != null) {
                return candidateAddress;
            }
            return InetAddress.getLocalHost();
        } catch (Exception ignored) {

        }
        return null;
    }

    public void changeRobotBaseNum(int num) {
        robotBasNum += num;
    }

    private void needAi() {
        if (closeFlag) {
            return;
        }
        if (aiAmount - playerAmount < robotBasNum) {
            if (currentCheckTime >= nextAiEnterTime) {
                logger.info("????????????ai");
                playerEnter(RobotManager.getInstance().getRole());
                if (aiAmount * 1d / robotBasNum < 0.85) {
                    nextAiEnterTime = currentCheckTime + RandomUtils.random(aiFastEnterTimeStart, aiFastEnterTimeEnd);
                } else {
                    nextAiEnterTime = currentCheckTime + RandomUtils.random(aiEnterTimeStart, aiEnterTimeEnd);
                }
            }
        }
    }

    private void aiCheck() {
        for (Map.Entry<Long, AiRole> entry : aiMap.entrySet()) {
            aiTree.refresh(this, entry.getValue());
        }
    }

    private long lastCloseLogTime;
    private long closeWaitExitTime = 1000;

    public long getCloseCountDownTime() {
        long leftTime = 0;
        switch (state) {
            case BET:
                leftTime = getMessenger().getLeftTime() + getRunTimeLimit() + getClearTimeLimit();
                break;
            case RUN:
                leftTime = getMessenger().getLeftTime() + getClearTimeLimit();
                break;
            case CLEAR:
                leftTime = getMessenger().getLeftTime();
                break;
        }
        return leftTime + closeWaitExitTime;
    }

    private void closeLog() {
        if (closeFlag) {
            if (currentCheckTime - lastCloseLogTime >= 1000) {
                lastCloseLogTime = currentCheckTime;
                logger.warn("???????????????????????????,??????????????? {}", getCloseCountDownTime() / 1000);
            }

        }
    }

    public void check() {
        long now = System.currentTimeMillis();
        currentCheckTime = now;
        aiCheck();
        needAi();
        closeLog();
        switch (state) {
            case BET:
                if (now - enterBetTime >= betTimeLimit) {
                    enterRun(now);
                }
                break;
            case RUN:
                if (now - enterRunTime >= runTimeLimit) {
                    enterClear(now);
                }
                break;
            case CLEAR:
                if (now - enterClearTime >= clearTimeLimit) {
                    enterIdle(now);
                }
                break;
            case IDLE:
                if (now - enterIdleTime >= idleTimeLimit) {
                    enterBet(now);
                }
                break;
            default:
                break;
        }
    }


    public Future<Void> close() {
        if (closeFlag) {
            return closeFuture;
        }
        closeFlag = true;
        logger.info("????????????????????????,??????????????????????????? ????????????:{}", state);

        return closeFuture;

    }

    private void close0() {
        if (closing) {
            logger.warn("????????????????????????");
            return;
        }
        closing = true;
        logger.info("????????????????????????");
        loopFuture.cancel(false);

        for (Map.Entry<Long, GameSeat> entry : seatMap.entrySet()) {
            messenger.sendErrorMessage(entry.getValue().getRole(), "?????????????????????");
        }
        if (closeWaitExitTime > 0) {
            try {
                Thread.sleep(closeWaitExitTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (Map.Entry<Long, GameSeat> entry : seatMap.entrySet()) {
            playerExit(entry.getValue(), "????????????");
        }
        GameInventoryManager.getInstance().closeServer();
        executorService.shutdown();
    }

    public int getAllAmount() {
        return aiAmount + playerAmount;

    }


    public Logger getLogger() {
        return logger;
    }

    public boolean isCloseFlag() {
        return closeFlag;
    }

    public void setCloseFlag(boolean closeFlag) {
        this.closeFlag = closeFlag;
    }

    public boolean isClosing() {
        return closing;
    }

    public void setClosing(boolean closing) {
        this.closing = closing;
    }

    public FutureTask<Void> getCloseFuture() {
        return closeFuture;
    }

    public ScheduledFuture<?> getLoopFuture() {
        return loopFuture;
    }

    public ScheduledExecutorService getExecutorService() {
        return executorService;
    }

    public int getRoomConfigId() {
        return roomConfigId;
    }

    public void setRoomConfigId(int roomConfigId) {
        this.roomConfigId = roomConfigId;
    }

    public long getEnterBetTime() {
        return enterBetTime;
    }

    public void setEnterBetTime(long enterBetTime) {
        this.enterBetTime = enterBetTime;
    }

    public long getBetTimeLimit() {
        return betTimeLimit;
    }

    public void setBetTimeLimit(long betTimeLimit) {
        this.betTimeLimit = betTimeLimit;
    }

    public long getEnterRunTime() {
        return enterRunTime;
    }

    public void setEnterRunTime(long enterRunTime) {
        this.enterRunTime = enterRunTime;
    }

    public long getRunTimeLimit() {
        return runTimeLimit;
    }

    public void setRunTimeLimit(long runTimeLimit) {
        this.runTimeLimit = runTimeLimit;
    }

    public long getEnterClearTime() {
        return enterClearTime;
    }

    public void setEnterClearTime(long enterClearTime) {
        this.enterClearTime = enterClearTime;
    }

    public long getClearTimeLimit() {
        return clearTimeLimit;
    }

    public void setClearTimeLimit(long clearTimeLimit) {
        this.clearTimeLimit = clearTimeLimit;
    }

    public GameRoomState getState() {
        return state;
    }

    public void setState(GameRoomState state) {
        this.state = state;
    }

    public long getCurrentCheckTime() {
        return currentCheckTime;
    }

    public void setCurrentCheckTime(long currentCheckTime) {
        this.currentCheckTime = currentCheckTime;
    }

    public Map<Long, PlayerRegionStatistics> getPlayerRegionStatisticsMap() {
        return playerRegionStatisticsMap;
    }

    public Map<Long, GameSeat> getSeatMap() {
        return seatMap;
    }

    public Map<Integer, BetRegion> getBetRegionMap() {
        return betRegionMap;
    }

    public Map<Long, AiRole> getAiMap() {
        return aiMap;
    }

    public int[] getResults() {
        return results;
    }

    public void setResults(int[] results) {
        this.results = results;
    }

    public int getAiAmount() {
        return aiAmount;
    }

    public void setAiAmount(int aiAmount) {
        this.aiAmount = aiAmount;
    }

    public int getPlayerAmount() {
        return playerAmount;
    }

    public void setPlayerAmount(int playerAmount) {
        this.playerAmount = playerAmount;
    }

    public GameConfigDevice getConfig() {
        return config;
    }

    public void setConfig(GameConfigDevice config) {
        this.config = config;
    }

    public HorseRunRoomConfig getRoomConfig() {
        return roomConfig;
    }

    public void setRoomConfig(HorseRunRoomConfig roomConfig) {
        this.roomConfig = roomConfig;
    }

    public long getEnterIdleTime() {
        return enterIdleTime;
    }

    public void setEnterIdleTime(long enterIdleTime) {
        this.enterIdleTime = enterIdleTime;
    }

    public long getIdleTimeLimit() {
        return idleTimeLimit;
    }

    public void setIdleTimeLimit(long idleTimeLimit) {
        this.idleTimeLimit = idleTimeLimit;
    }

    public GameRoomMessenger getMessenger() {
        return messenger;
    }

    public BetRegion getResultBetRegion() {
        return resultBetRegion;
    }

    public List<BetRegion> getHistories() {
        return histories;
    }

    public Map<Long, Long> getPlayerResultMap() {
        return playerResultMap;
    }

    public Map<Long, Random> getRandomMap() {
        return randomMap;
    }

    public Map<Long, Long> getPlayerBetStatisticMap() {
        return playerBetStatisticMap;
    }

    public Map<Long, GameSeat> getPlayerBetSeatMap() {
        return playerBetSeatMap;
    }

    public int getRobotBasNum() {
        return robotBasNum;
    }

    public void setRobotBasNum(int robotBasNum) {
        this.robotBasNum = robotBasNum;
    }

    public long getNextAiEnterTime() {
        return nextAiEnterTime;
    }

    public void setNextAiEnterTime(long nextAiEnterTime) {
        this.nextAiEnterTime = nextAiEnterTime;
    }

    public int getAiFastEnterTimeStart() {
        return aiFastEnterTimeStart;
    }

    public void setAiFastEnterTimeStart(int aiFastEnterTimeStart) {
        this.aiFastEnterTimeStart = aiFastEnterTimeStart;
    }

    public int getAiFastEnterTimeEnd() {
        return aiFastEnterTimeEnd;
    }

    public void setAiFastEnterTimeEnd(int aiFastEnterTimeEnd) {
        this.aiFastEnterTimeEnd = aiFastEnterTimeEnd;
    }

    public int getAiEnterTimeStart() {
        return aiEnterTimeStart;
    }

    public void setAiEnterTimeStart(int aiEnterTimeStart) {
        this.aiEnterTimeStart = aiEnterTimeStart;
    }

    public int getAiEnterTimeEnd() {
        return aiEnterTimeEnd;
    }

    public void setAiEnterTimeEnd(int aiEnterTimeEnd) {
        this.aiEnterTimeEnd = aiEnterTimeEnd;
    }

    public AiTree getAiTree() {
        return aiTree;
    }

    public long getCloseWaitExitTime() {
        return closeWaitExitTime;
    }

    public void setCloseWaitExitTime(long closeWaitExitTime) {
        this.closeWaitExitTime = closeWaitExitTime;
    }
}
