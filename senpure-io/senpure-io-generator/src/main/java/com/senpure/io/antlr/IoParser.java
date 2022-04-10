// Generated from E:/Projects/senpure/senpure-io/senpure-io-generator/src/main/resources\Io.g4 by ANTLR 4.7.2
package com.senpure.io.antlr;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class IoParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		T__24=25, T__25=26, T__26=27, T__27=28, T__28=29, T__29=30, ABSTRACT=31, 
		ASSERT=32, BOOLEAN=33, BREAK=34, BYTE=35, CASE=36, CATCH=37, CHAR=38, 
		CLASS=39, CONST=40, CONTINUE=41, DEFAULT=42, DO=43, DOUBLE=44, ELSE=45, 
		ENUM=46, EXTENDS=47, FINAL=48, FINALLY=49, FLOAT=50, FOR=51, IF=52, GOTO=53, 
		IMPLEMENTS=54, IMPORT=55, INSTANCEOF=56, INT=57, INTERFACE=58, LONG=59, 
		NATIVE=60, NEW=61, PACKAGE=62, PRIVATE=63, PROTECTED=64, PUBLIC=65, RETURN=66, 
		SHORT=67, STATIC=68, STRICTFP=69, SUPER=70, SWITCH=71, SYNCHRONIZED=72, 
		THIS=73, THROW=74, THROWS=75, TRANSIENT=76, TRY=77, VOID=78, VOLATILE=79, 
		WHILE=80, SELF=81, MESSAGEID=82, EVENTID=83, SERIALIZEDSIZE=84, UNKNOWN=85, 
		Number=86, Identifier=87, WS=88, COMMENT=89, LINE_COMMENT=90, CODE_COMMENT=91;
	public static final int
		RULE_protocol = 0, RULE_headContent = 1, RULE_entity = 2, RULE_importIo = 3, 
		RULE_importValue = 4, RULE_javaPackage = 5, RULE_javaPackageValue = 6, 
		RULE_namespace = 7, RULE_namespaceValue = 8, RULE_luaNamespace = 9, RULE_luaNamespaceValue = 10, 
		RULE_jsNamespace = 11, RULE_jsNamespaceValue = 12, RULE_fileName = 13, 
		RULE_filePath = 14, RULE_message = 15, RULE_messageHead = 16, RULE_messageType = 17, 
		RULE_messageName = 18, RULE_messageId = 19, RULE_entityComment = 20, RULE_event = 21, 
		RULE_eventHead = 22, RULE_eventName = 23, RULE_eventId = 24, RULE_bean = 25, 
		RULE_beanHead = 26, RULE_beanName = 27, RULE_field = 28, RULE_fieldList = 29, 
		RULE_fieldIndex = 30, RULE_fieldType = 31, RULE_fieldName = 32, RULE_fieldComment = 33, 
		RULE_enumSymbol = 34, RULE_enumField = 35, RULE_enumHead = 36, RULE_enumName = 37;
	private static String[] makeRuleNames() {
		return new String[] {
			"protocol", "headContent", "entity", "importIo", "importValue", "javaPackage", 
			"javaPackageValue", "namespace", "namespaceValue", "luaNamespace", "luaNamespaceValue", 
			"jsNamespace", "jsNamespaceValue", "fileName", "filePath", "message", 
			"messageHead", "messageType", "messageName", "messageId", "entityComment", 
			"event", "eventHead", "eventName", "eventId", "bean", "beanHead", "beanName", 
			"field", "fieldList", "fieldIndex", "fieldType", "fieldName", "fieldComment", 
			"enumSymbol", "enumField", "enumHead", "enumName"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "';'", "'.'", "'io'", "'javaPackage'", "'namespace'", "'luaNamespace'", 
			"'jsNamespace'", "'cs'", "'CS'", "'sc'", "'SC'", "'bean'", "'message'", 
			"'event'", "'bytes'", "'-'", "'../'", "'/'", "'\\'", "'{'", "'}'", "'='", 
			"'['", "']'", "'sint'", "'slong'", "'fixed32'", "'fixed64'", "'String'", 
			"'string'", "'abstract'", "'assert'", "'boolean'", "'break'", "'byte'", 
			"'case'", "'catch'", "'char'", "'class'", "'const'", "'continue'", "'default'", 
			"'do'", "'double'", "'else'", "'enum'", "'extends'", "'final'", "'finally'", 
			"'float'", "'for'", "'if'", "'goto'", "'implements'", "'import'", "'instanceof'", 
			"'int'", "'interface'", "'long'", "'native'", "'new'", "'package'", "'private'", 
			"'protected'", "'public'", "'return'", "'short'", "'static'", "'strictfp'", 
			"'super'", "'switch'", "'synchronized'", "'this'", "'throw'", "'throws'", 
			"'transient'", "'try'", "'void'", "'volatile'", "'while'", "'self'", 
			"'messageId'", "'eventId'", "'serializedSize'", "'UNKNOWN'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, null, null, "ABSTRACT", "ASSERT", "BOOLEAN", 
			"BREAK", "BYTE", "CASE", "CATCH", "CHAR", "CLASS", "CONST", "CONTINUE", 
			"DEFAULT", "DO", "DOUBLE", "ELSE", "ENUM", "EXTENDS", "FINAL", "FINALLY", 
			"FLOAT", "FOR", "IF", "GOTO", "IMPLEMENTS", "IMPORT", "INSTANCEOF", "INT", 
			"INTERFACE", "LONG", "NATIVE", "NEW", "PACKAGE", "PRIVATE", "PROTECTED", 
			"PUBLIC", "RETURN", "SHORT", "STATIC", "STRICTFP", "SUPER", "SWITCH", 
			"SYNCHRONIZED", "THIS", "THROW", "THROWS", "TRANSIENT", "TRY", "VOID", 
			"VOLATILE", "WHILE", "SELF", "MESSAGEID", "EVENTID", "SERIALIZEDSIZE", 
			"UNKNOWN", "Number", "Identifier", "WS", "COMMENT", "LINE_COMMENT", "CODE_COMMENT"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Io.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public IoParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProtocolContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(IoParser.EOF, 0); }
		public List<HeadContentContext> headContent() {
			return getRuleContexts(HeadContentContext.class);
		}
		public HeadContentContext headContent(int i) {
			return getRuleContext(HeadContentContext.class,i);
		}
		public List<EntityContext> entity() {
			return getRuleContexts(EntityContext.class);
		}
		public EntityContext entity(int i) {
			return getRuleContext(EntityContext.class,i);
		}
		public ProtocolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_protocol; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterProtocol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitProtocol(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitProtocol(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProtocolContext protocol() throws RecognitionException {
		ProtocolContext _localctx = new ProtocolContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_protocol);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6) | (1L << IMPORT))) != 0)) {
				{
				{
				setState(76);
				headContent();
				}
				}
				setState(81);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(85);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__11) | (1L << T__12) | (1L << T__13) | (1L << ENUM))) != 0) || _la==LINE_COMMENT) {
				{
				{
				setState(82);
				entity();
				}
				}
				setState(87);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(88);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class HeadContentContext extends ParserRuleContext {
		public ImportIoContext importIo() {
			return getRuleContext(ImportIoContext.class,0);
		}
		public NamespaceContext namespace() {
			return getRuleContext(NamespaceContext.class,0);
		}
		public JavaPackageContext javaPackage() {
			return getRuleContext(JavaPackageContext.class,0);
		}
		public LuaNamespaceContext luaNamespace() {
			return getRuleContext(LuaNamespaceContext.class,0);
		}
		public JsNamespaceContext jsNamespace() {
			return getRuleContext(JsNamespaceContext.class,0);
		}
		public HeadContentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_headContent; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterHeadContent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitHeadContent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitHeadContent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HeadContentContext headContent() throws RecognitionException {
		HeadContentContext _localctx = new HeadContentContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_headContent);
		try {
			setState(95);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case IMPORT:
				enterOuterAlt(_localctx, 1);
				{
				setState(90);
				importIo();
				}
				break;
			case T__4:
				enterOuterAlt(_localctx, 2);
				{
				setState(91);
				namespace();
				}
				break;
			case T__3:
				enterOuterAlt(_localctx, 3);
				{
				setState(92);
				javaPackage();
				}
				break;
			case T__5:
				enterOuterAlt(_localctx, 4);
				{
				setState(93);
				luaNamespace();
				}
				break;
			case T__6:
				enterOuterAlt(_localctx, 5);
				{
				setState(94);
				jsNamespace();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EntityContext extends ParserRuleContext {
		public MessageContext message() {
			return getRuleContext(MessageContext.class,0);
		}
		public EventContext event() {
			return getRuleContext(EventContext.class,0);
		}
		public BeanContext bean() {
			return getRuleContext(BeanContext.class,0);
		}
		public EnumSymbolContext enumSymbol() {
			return getRuleContext(EnumSymbolContext.class,0);
		}
		public EntityContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entity; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterEntity(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitEntity(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitEntity(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EntityContext entity() throws RecognitionException {
		EntityContext _localctx = new EntityContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_entity);
		try {
			setState(101);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(97);
				message();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(98);
				event();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(99);
				bean();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(100);
				enumSymbol();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImportIoContext extends ParserRuleContext {
		public TerminalNode IMPORT() { return getToken(IoParser.IMPORT, 0); }
		public ImportValueContext importValue() {
			return getRuleContext(ImportValueContext.class,0);
		}
		public ImportIoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importIo; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterImportIo(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitImportIo(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitImportIo(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportIoContext importIo() throws RecognitionException {
		ImportIoContext _localctx = new ImportIoContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_importIo);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			match(IMPORT);
			setState(104);
			importValue();
			setState(105);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ImportValueContext extends ParserRuleContext {
		public FilePathContext filePath() {
			return getRuleContext(FilePathContext.class,0);
		}
		public ImportValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_importValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterImportValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitImportValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitImportValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ImportValueContext importValue() throws RecognitionException {
		ImportValueContext _localctx = new ImportValueContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_importValue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			filePath(0);
			setState(108);
			match(T__1);
			setState(109);
			match(T__2);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JavaPackageContext extends ParserRuleContext {
		public JavaPackageValueContext javaPackageValue() {
			return getRuleContext(JavaPackageValueContext.class,0);
		}
		public JavaPackageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_javaPackage; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterJavaPackage(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitJavaPackage(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitJavaPackage(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JavaPackageContext javaPackage() throws RecognitionException {
		JavaPackageContext _localctx = new JavaPackageContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_javaPackage);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(111);
			match(T__3);
			setState(112);
			javaPackageValue(0);
			setState(113);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JavaPackageValueContext extends ParserRuleContext {
		public FileNameContext fileName() {
			return getRuleContext(FileNameContext.class,0);
		}
		public JavaPackageValueContext javaPackageValue() {
			return getRuleContext(JavaPackageValueContext.class,0);
		}
		public JavaPackageValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_javaPackageValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterJavaPackageValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitJavaPackageValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitJavaPackageValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JavaPackageValueContext javaPackageValue() throws RecognitionException {
		return javaPackageValue(0);
	}

	private JavaPackageValueContext javaPackageValue(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		JavaPackageValueContext _localctx = new JavaPackageValueContext(_ctx, _parentState);
		JavaPackageValueContext _prevctx = _localctx;
		int _startState = 12;
		enterRecursionRule(_localctx, 12, RULE_javaPackageValue, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(116);
			fileName();
			}
			_ctx.stop = _input.LT(-1);
			setState(123);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new JavaPackageValueContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_javaPackageValue);
					setState(118);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(119);
					match(T__1);
					setState(120);
					fileName();
					}
					} 
				}
				setState(125);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,4,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class NamespaceContext extends ParserRuleContext {
		public NamespaceValueContext namespaceValue() {
			return getRuleContext(NamespaceValueContext.class,0);
		}
		public NamespaceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespace; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterNamespace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitNamespace(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitNamespace(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceContext namespace() throws RecognitionException {
		NamespaceContext _localctx = new NamespaceContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_namespace);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			match(T__4);
			setState(127);
			namespaceValue(0);
			setState(128);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NamespaceValueContext extends ParserRuleContext {
		public FileNameContext fileName() {
			return getRuleContext(FileNameContext.class,0);
		}
		public NamespaceValueContext namespaceValue() {
			return getRuleContext(NamespaceValueContext.class,0);
		}
		public NamespaceValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespaceValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterNamespaceValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitNamespaceValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitNamespaceValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NamespaceValueContext namespaceValue() throws RecognitionException {
		return namespaceValue(0);
	}

	private NamespaceValueContext namespaceValue(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		NamespaceValueContext _localctx = new NamespaceValueContext(_ctx, _parentState);
		NamespaceValueContext _prevctx = _localctx;
		int _startState = 16;
		enterRecursionRule(_localctx, 16, RULE_namespaceValue, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(131);
			fileName();
			}
			_ctx.stop = _input.LT(-1);
			setState(138);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new NamespaceValueContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_namespaceValue);
					setState(133);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(134);
					match(T__1);
					setState(135);
					fileName();
					}
					} 
				}
				setState(140);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class LuaNamespaceContext extends ParserRuleContext {
		public LuaNamespaceValueContext luaNamespaceValue() {
			return getRuleContext(LuaNamespaceValueContext.class,0);
		}
		public LuaNamespaceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_luaNamespace; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterLuaNamespace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitLuaNamespace(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitLuaNamespace(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LuaNamespaceContext luaNamespace() throws RecognitionException {
		LuaNamespaceContext _localctx = new LuaNamespaceContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_luaNamespace);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			match(T__5);
			setState(142);
			luaNamespaceValue(0);
			setState(143);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LuaNamespaceValueContext extends ParserRuleContext {
		public FileNameContext fileName() {
			return getRuleContext(FileNameContext.class,0);
		}
		public LuaNamespaceValueContext luaNamespaceValue() {
			return getRuleContext(LuaNamespaceValueContext.class,0);
		}
		public LuaNamespaceValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_luaNamespaceValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterLuaNamespaceValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitLuaNamespaceValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitLuaNamespaceValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LuaNamespaceValueContext luaNamespaceValue() throws RecognitionException {
		return luaNamespaceValue(0);
	}

	private LuaNamespaceValueContext luaNamespaceValue(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		LuaNamespaceValueContext _localctx = new LuaNamespaceValueContext(_ctx, _parentState);
		LuaNamespaceValueContext _prevctx = _localctx;
		int _startState = 20;
		enterRecursionRule(_localctx, 20, RULE_luaNamespaceValue, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(146);
			fileName();
			}
			_ctx.stop = _input.LT(-1);
			setState(153);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new LuaNamespaceValueContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_luaNamespaceValue);
					setState(148);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(149);
					match(T__1);
					setState(150);
					fileName();
					}
					} 
				}
				setState(155);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class JsNamespaceContext extends ParserRuleContext {
		public JsNamespaceValueContext jsNamespaceValue() {
			return getRuleContext(JsNamespaceValueContext.class,0);
		}
		public JsNamespaceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsNamespace; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterJsNamespace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitJsNamespace(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitJsNamespace(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsNamespaceContext jsNamespace() throws RecognitionException {
		JsNamespaceContext _localctx = new JsNamespaceContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_jsNamespace);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(156);
			match(T__6);
			setState(157);
			jsNamespaceValue(0);
			setState(158);
			match(T__0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JsNamespaceValueContext extends ParserRuleContext {
		public FileNameContext fileName() {
			return getRuleContext(FileNameContext.class,0);
		}
		public JsNamespaceValueContext jsNamespaceValue() {
			return getRuleContext(JsNamespaceValueContext.class,0);
		}
		public JsNamespaceValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_jsNamespaceValue; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterJsNamespaceValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitJsNamespaceValue(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitJsNamespaceValue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JsNamespaceValueContext jsNamespaceValue() throws RecognitionException {
		return jsNamespaceValue(0);
	}

	private JsNamespaceValueContext jsNamespaceValue(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		JsNamespaceValueContext _localctx = new JsNamespaceValueContext(_ctx, _parentState);
		JsNamespaceValueContext _prevctx = _localctx;
		int _startState = 24;
		enterRecursionRule(_localctx, 24, RULE_jsNamespaceValue, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(161);
			fileName();
			}
			_ctx.stop = _input.LT(-1);
			setState(168);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new JsNamespaceValueContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_jsNamespaceValue);
					setState(163);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(164);
					match(T__1);
					setState(165);
					fileName();
					}
					} 
				}
				setState(170);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class FileNameContext extends ParserRuleContext {
		public List<TerminalNode> Number() { return getTokens(IoParser.Number); }
		public TerminalNode Number(int i) {
			return getToken(IoParser.Number, i);
		}
		public List<TerminalNode> Identifier() { return getTokens(IoParser.Identifier); }
		public TerminalNode Identifier(int i) {
			return getToken(IoParser.Identifier, i);
		}
		public FileNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fileName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterFileName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitFileName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitFileName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FileNameContext fileName() throws RecognitionException {
		FileNameContext _localctx = new FileNameContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_fileName);
		try {
			int _alt;
			setState(193);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				{
				setState(174);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(171);
						match(Number);
						}
						} 
					}
					setState(176);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
				}
				setState(180);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(177);
						match(Identifier);
						}
						} 
					}
					setState(182);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,9,_ctx);
				}
				}
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(183);
				match(T__7);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(184);
				match(T__8);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(185);
				match(T__9);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(186);
				match(T__10);
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(187);
				match(T__11);
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(188);
				match(T__12);
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(189);
				match(T__13);
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(190);
				match(T__2);
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(191);
				match(T__4);
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(192);
				match(T__14);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FilePathContext extends ParserRuleContext {
		public FileNameContext fileName() {
			return getRuleContext(FileNameContext.class,0);
		}
		public FilePathContext filePath() {
			return getRuleContext(FilePathContext.class,0);
		}
		public FilePathContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filePath; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterFilePath(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitFilePath(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitFilePath(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FilePathContext filePath() throws RecognitionException {
		return filePath(0);
	}

	private FilePathContext filePath(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		FilePathContext _localctx = new FilePathContext(_ctx, _parentState);
		FilePathContext _prevctx = _localctx;
		int _startState = 28;
		enterRecursionRule(_localctx, 28, RULE_filePath, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			setState(196);
			fileName();
			}
			_ctx.stop = _input.LT(-1);
			setState(203);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new FilePathContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_filePath);
					setState(198);
					if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
					setState(199);
					_la = _input.LA(1);
					if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__1) | (1L << T__15) | (1L << T__16) | (1L << T__17) | (1L << T__18))) != 0)) ) {
					_errHandler.recoverInline(this);
					}
					else {
						if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
						_errHandler.reportMatch(this);
						consume();
					}
					setState(200);
					fileName();
					}
					} 
				}
				setState(205);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,11,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class MessageContext extends ParserRuleContext {
		public MessageHeadContext messageHead() {
			return getRuleContext(MessageHeadContext.class,0);
		}
		public MessageTypeContext messageType() {
			return getRuleContext(MessageTypeContext.class,0);
		}
		public MessageNameContext messageName() {
			return getRuleContext(MessageNameContext.class,0);
		}
		public MessageIdContext messageId() {
			return getRuleContext(MessageIdContext.class,0);
		}
		public List<EntityCommentContext> entityComment() {
			return getRuleContexts(EntityCommentContext.class);
		}
		public EntityCommentContext entityComment(int i) {
			return getRuleContext(EntityCommentContext.class,i);
		}
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public MessageContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_message; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterMessage(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitMessage(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitMessage(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MessageContext message() throws RecognitionException {
		MessageContext _localctx = new MessageContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_message);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(209);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LINE_COMMENT) {
				{
				{
				setState(206);
				entityComment();
				}
				}
				setState(211);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(212);
			messageHead();
			setState(213);
			messageType();
			setState(214);
			messageName();
			setState(215);
			messageId();
			setState(216);
			match(T__19);
			setState(220);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << BOOLEAN) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG))) != 0) || _la==Identifier) {
				{
				{
				setState(217);
				field();
				}
				}
				setState(222);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(223);
			match(T__20);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MessageHeadContext extends ParserRuleContext {
		public MessageHeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_messageHead; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterMessageHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitMessageHead(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitMessageHead(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MessageHeadContext messageHead() throws RecognitionException {
		MessageHeadContext _localctx = new MessageHeadContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_messageHead);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(225);
			match(T__12);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MessageTypeContext extends ParserRuleContext {
		public MessageTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_messageType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterMessageType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitMessageType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitMessageType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MessageTypeContext messageType() throws RecognitionException {
		MessageTypeContext _localctx = new MessageTypeContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_messageType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(227);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__7) | (1L << T__8) | (1L << T__9) | (1L << T__10))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MessageNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(IoParser.Identifier, 0); }
		public MessageNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_messageName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterMessageName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitMessageName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitMessageName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MessageNameContext messageName() throws RecognitionException {
		MessageNameContext _localctx = new MessageNameContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_messageName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(229);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MessageIdContext extends ParserRuleContext {
		public TerminalNode Number() { return getToken(IoParser.Number, 0); }
		public MessageIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_messageId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterMessageId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitMessageId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitMessageId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MessageIdContext messageId() throws RecognitionException {
		MessageIdContext _localctx = new MessageIdContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_messageId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(231);
			match(Number);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EntityCommentContext extends ParserRuleContext {
		public TerminalNode LINE_COMMENT() { return getToken(IoParser.LINE_COMMENT, 0); }
		public EntityCommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_entityComment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterEntityComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitEntityComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitEntityComment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EntityCommentContext entityComment() throws RecognitionException {
		EntityCommentContext _localctx = new EntityCommentContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_entityComment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(233);
			match(LINE_COMMENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EventContext extends ParserRuleContext {
		public EventHeadContext eventHead() {
			return getRuleContext(EventHeadContext.class,0);
		}
		public EventNameContext eventName() {
			return getRuleContext(EventNameContext.class,0);
		}
		public EventIdContext eventId() {
			return getRuleContext(EventIdContext.class,0);
		}
		public List<EntityCommentContext> entityComment() {
			return getRuleContexts(EntityCommentContext.class);
		}
		public EntityCommentContext entityComment(int i) {
			return getRuleContext(EntityCommentContext.class,i);
		}
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public EventContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_event; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterEvent(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitEvent(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitEvent(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EventContext event() throws RecognitionException {
		EventContext _localctx = new EventContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_event);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(238);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LINE_COMMENT) {
				{
				{
				setState(235);
				entityComment();
				}
				}
				setState(240);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(241);
			eventHead();
			setState(242);
			eventName();
			setState(243);
			eventId();
			setState(244);
			match(T__19);
			setState(248);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << BOOLEAN) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG))) != 0) || _la==Identifier) {
				{
				{
				setState(245);
				field();
				}
				}
				setState(250);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(251);
			match(T__20);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EventHeadContext extends ParserRuleContext {
		public EventHeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eventHead; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterEventHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitEventHead(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitEventHead(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EventHeadContext eventHead() throws RecognitionException {
		EventHeadContext _localctx = new EventHeadContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_eventHead);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(253);
			match(T__13);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EventNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(IoParser.Identifier, 0); }
		public EventNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eventName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterEventName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitEventName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitEventName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EventNameContext eventName() throws RecognitionException {
		EventNameContext _localctx = new EventNameContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_eventName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(255);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EventIdContext extends ParserRuleContext {
		public TerminalNode Number() { return getToken(IoParser.Number, 0); }
		public EventIdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_eventId; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterEventId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitEventId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitEventId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EventIdContext eventId() throws RecognitionException {
		EventIdContext _localctx = new EventIdContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_eventId);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(257);
			match(Number);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BeanContext extends ParserRuleContext {
		public BeanHeadContext beanHead() {
			return getRuleContext(BeanHeadContext.class,0);
		}
		public BeanNameContext beanName() {
			return getRuleContext(BeanNameContext.class,0);
		}
		public List<EntityCommentContext> entityComment() {
			return getRuleContexts(EntityCommentContext.class);
		}
		public EntityCommentContext entityComment(int i) {
			return getRuleContext(EntityCommentContext.class,i);
		}
		public List<FieldContext> field() {
			return getRuleContexts(FieldContext.class);
		}
		public FieldContext field(int i) {
			return getRuleContext(FieldContext.class,i);
		}
		public BeanContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bean; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterBean(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitBean(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitBean(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BeanContext bean() throws RecognitionException {
		BeanContext _localctx = new BeanContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_bean);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LINE_COMMENT) {
				{
				{
				setState(259);
				entityComment();
				}
				}
				setState(264);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(265);
			beanHead();
			setState(266);
			beanName();
			setState(267);
			match(T__19);
			setState(269); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(268);
				field();
				}
				}
				setState(271); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << BOOLEAN) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG))) != 0) || _la==Identifier );
			setState(273);
			match(T__20);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BeanHeadContext extends ParserRuleContext {
		public BeanHeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_beanHead; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterBeanHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitBeanHead(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitBeanHead(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BeanHeadContext beanHead() throws RecognitionException {
		BeanHeadContext _localctx = new BeanHeadContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_beanHead);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(275);
			match(T__11);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BeanNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(IoParser.Identifier, 0); }
		public BeanNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_beanName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterBeanName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitBeanName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitBeanName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BeanNameContext beanName() throws RecognitionException {
		BeanNameContext _localctx = new BeanNameContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_beanName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(277);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldContext extends ParserRuleContext {
		public FieldTypeContext fieldType() {
			return getRuleContext(FieldTypeContext.class,0);
		}
		public FieldNameContext fieldName() {
			return getRuleContext(FieldNameContext.class,0);
		}
		public FieldListContext fieldList() {
			return getRuleContext(FieldListContext.class,0);
		}
		public FieldIndexContext fieldIndex() {
			return getRuleContext(FieldIndexContext.class,0);
		}
		public FieldCommentContext fieldComment() {
			return getRuleContext(FieldCommentContext.class,0);
		}
		public FieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_field; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldContext field() throws RecognitionException {
		FieldContext _localctx = new FieldContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_field);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(279);
			fieldType();
			setState(281);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__22) {
				{
				setState(280);
				fieldList();
				}
			}

			setState(283);
			fieldName();
			setState(286);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__21) {
				{
				setState(284);
				match(T__21);
				setState(285);
				fieldIndex();
				}
			}

			setState(288);
			match(T__0);
			setState(290);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LINE_COMMENT) {
				{
				setState(289);
				fieldComment();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldListContext extends ParserRuleContext {
		public FieldListContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldList; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterFieldList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitFieldList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitFieldList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldListContext fieldList() throws RecognitionException {
		FieldListContext _localctx = new FieldListContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_fieldList);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(292);
			match(T__22);
			setState(293);
			match(T__23);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldIndexContext extends ParserRuleContext {
		public TerminalNode Number() { return getToken(IoParser.Number, 0); }
		public FieldIndexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldIndex; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterFieldIndex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitFieldIndex(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitFieldIndex(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldIndexContext fieldIndex() throws RecognitionException {
		FieldIndexContext _localctx = new FieldIndexContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_fieldIndex);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			match(Number);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldTypeContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(IoParser.INT, 0); }
		public TerminalNode LONG() { return getToken(IoParser.LONG, 0); }
		public TerminalNode FLOAT() { return getToken(IoParser.FLOAT, 0); }
		public TerminalNode DOUBLE() { return getToken(IoParser.DOUBLE, 0); }
		public TerminalNode BOOLEAN() { return getToken(IoParser.BOOLEAN, 0); }
		public TerminalNode Identifier() { return getToken(IoParser.Identifier, 0); }
		public FieldTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterFieldType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitFieldType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitFieldType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldTypeContext fieldType() throws RecognitionException {
		FieldTypeContext _localctx = new FieldTypeContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_fieldType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(297);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__14) | (1L << T__24) | (1L << T__25) | (1L << T__26) | (1L << T__27) | (1L << T__28) | (1L << T__29) | (1L << BOOLEAN) | (1L << DOUBLE) | (1L << FLOAT) | (1L << INT) | (1L << LONG))) != 0) || _la==Identifier) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(IoParser.Identifier, 0); }
		public MessageHeadContext messageHead() {
			return getRuleContext(MessageHeadContext.class,0);
		}
		public BeanHeadContext beanHead() {
			return getRuleContext(BeanHeadContext.class,0);
		}
		public EventHeadContext eventHead() {
			return getRuleContext(EventHeadContext.class,0);
		}
		public FieldNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterFieldName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitFieldName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitFieldName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldNameContext fieldName() throws RecognitionException {
		FieldNameContext _localctx = new FieldNameContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_fieldName);
		try {
			setState(303);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case Identifier:
				enterOuterAlt(_localctx, 1);
				{
				setState(299);
				match(Identifier);
				}
				break;
			case T__12:
				enterOuterAlt(_localctx, 2);
				{
				setState(300);
				messageHead();
				}
				break;
			case T__11:
				enterOuterAlt(_localctx, 3);
				{
				setState(301);
				beanHead();
				}
				break;
			case T__13:
				enterOuterAlt(_localctx, 4);
				{
				setState(302);
				eventHead();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FieldCommentContext extends ParserRuleContext {
		public TerminalNode LINE_COMMENT() { return getToken(IoParser.LINE_COMMENT, 0); }
		public FieldCommentContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_fieldComment; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterFieldComment(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitFieldComment(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitFieldComment(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FieldCommentContext fieldComment() throws RecognitionException {
		FieldCommentContext _localctx = new FieldCommentContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_fieldComment);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(305);
			match(LINE_COMMENT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumSymbolContext extends ParserRuleContext {
		public EnumHeadContext enumHead() {
			return getRuleContext(EnumHeadContext.class,0);
		}
		public EnumNameContext enumName() {
			return getRuleContext(EnumNameContext.class,0);
		}
		public List<EnumFieldContext> enumField() {
			return getRuleContexts(EnumFieldContext.class);
		}
		public EnumFieldContext enumField(int i) {
			return getRuleContext(EnumFieldContext.class,i);
		}
		public List<EntityCommentContext> entityComment() {
			return getRuleContexts(EntityCommentContext.class);
		}
		public EntityCommentContext entityComment(int i) {
			return getRuleContext(EntityCommentContext.class,i);
		}
		public EnumSymbolContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumSymbol; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterEnumSymbol(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitEnumSymbol(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitEnumSymbol(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumSymbolContext enumSymbol() throws RecognitionException {
		EnumSymbolContext _localctx = new EnumSymbolContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_enumSymbol);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(310);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==LINE_COMMENT) {
				{
				{
				setState(307);
				entityComment();
				}
				}
				setState(312);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(313);
			enumHead();
			setState(314);
			enumName();
			setState(315);
			match(T__19);
			setState(316);
			enumField();
			setState(318); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(317);
				enumField();
				}
				}
				setState(320); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__11) | (1L << T__12) | (1L << T__13))) != 0) || _la==Identifier );
			setState(322);
			match(T__20);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumFieldContext extends ParserRuleContext {
		public FieldNameContext fieldName() {
			return getRuleContext(FieldNameContext.class,0);
		}
		public FieldIndexContext fieldIndex() {
			return getRuleContext(FieldIndexContext.class,0);
		}
		public FieldCommentContext fieldComment() {
			return getRuleContext(FieldCommentContext.class,0);
		}
		public EnumFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumField; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterEnumField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitEnumField(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitEnumField(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumFieldContext enumField() throws RecognitionException {
		EnumFieldContext _localctx = new EnumFieldContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_enumField);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(324);
			fieldName();
			setState(327);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__21) {
				{
				setState(325);
				match(T__21);
				setState(326);
				fieldIndex();
				}
			}

			setState(329);
			match(T__0);
			setState(331);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LINE_COMMENT) {
				{
				setState(330);
				fieldComment();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumHeadContext extends ParserRuleContext {
		public TerminalNode ENUM() { return getToken(IoParser.ENUM, 0); }
		public EnumHeadContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumHead; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterEnumHead(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitEnumHead(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitEnumHead(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumHeadContext enumHead() throws RecognitionException {
		EnumHeadContext _localctx = new EnumHeadContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_enumHead);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(333);
			match(ENUM);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class EnumNameContext extends ParserRuleContext {
		public TerminalNode Identifier() { return getToken(IoParser.Identifier, 0); }
		public EnumNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).enterEnumName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof IoListener ) ((IoListener)listener).exitEnumName(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof IoVisitor ) return ((IoVisitor<? extends T>)visitor).visitEnumName(this);
			else return visitor.visitChildren(this);
		}
	}

	public final EnumNameContext enumName() throws RecognitionException {
		EnumNameContext _localctx = new EnumNameContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_enumName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(335);
			match(Identifier);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 6:
			return javaPackageValue_sempred((JavaPackageValueContext)_localctx, predIndex);
		case 8:
			return namespaceValue_sempred((NamespaceValueContext)_localctx, predIndex);
		case 10:
			return luaNamespaceValue_sempred((LuaNamespaceValueContext)_localctx, predIndex);
		case 12:
			return jsNamespaceValue_sempred((JsNamespaceValueContext)_localctx, predIndex);
		case 14:
			return filePath_sempred((FilePathContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean javaPackageValue_sempred(JavaPackageValueContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean namespaceValue_sempred(NamespaceValueContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean luaNamespaceValue_sempred(LuaNamespaceValueContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean jsNamespaceValue_sempred(JsNamespaceValueContext _localctx, int predIndex) {
		switch (predIndex) {
		case 3:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean filePath_sempred(FilePathContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3]\u0154\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\3\2\7\2P\n\2\f\2\16\2S\13"+
		"\2\3\2\7\2V\n\2\f\2\16\2Y\13\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\5\3b\n\3\3"+
		"\4\3\4\3\4\3\4\5\4h\n\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\b\7\b|\n\b\f\b\16\b\177\13\b\3\t\3\t\3\t\3\t"+
		"\3\n\3\n\3\n\3\n\3\n\3\n\7\n\u008b\n\n\f\n\16\n\u008e\13\n\3\13\3\13\3"+
		"\13\3\13\3\f\3\f\3\f\3\f\3\f\3\f\7\f\u009a\n\f\f\f\16\f\u009d\13\f\3\r"+
		"\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\7\16\u00a9\n\16\f\16\16\16"+
		"\u00ac\13\16\3\17\7\17\u00af\n\17\f\17\16\17\u00b2\13\17\3\17\7\17\u00b5"+
		"\n\17\f\17\16\17\u00b8\13\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\5\17\u00c4\n\17\3\20\3\20\3\20\3\20\3\20\3\20\7\20\u00cc\n\20"+
		"\f\20\16\20\u00cf\13\20\3\21\7\21\u00d2\n\21\f\21\16\21\u00d5\13\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\7\21\u00dd\n\21\f\21\16\21\u00e0\13\21\3"+
		"\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\26\3\26\3\27\7\27\u00ef"+
		"\n\27\f\27\16\27\u00f2\13\27\3\27\3\27\3\27\3\27\3\27\7\27\u00f9\n\27"+
		"\f\27\16\27\u00fc\13\27\3\27\3\27\3\30\3\30\3\31\3\31\3\32\3\32\3\33\7"+
		"\33\u0107\n\33\f\33\16\33\u010a\13\33\3\33\3\33\3\33\3\33\6\33\u0110\n"+
		"\33\r\33\16\33\u0111\3\33\3\33\3\34\3\34\3\35\3\35\3\36\3\36\5\36\u011c"+
		"\n\36\3\36\3\36\3\36\5\36\u0121\n\36\3\36\3\36\5\36\u0125\n\36\3\37\3"+
		"\37\3\37\3 \3 \3!\3!\3\"\3\"\3\"\3\"\5\"\u0132\n\"\3#\3#\3$\7$\u0137\n"+
		"$\f$\16$\u013a\13$\3$\3$\3$\3$\3$\6$\u0141\n$\r$\16$\u0142\3$\3$\3%\3"+
		"%\3%\5%\u014a\n%\3%\3%\5%\u014e\n%\3&\3&\3\'\3\'\3\'\2\7\16\22\26\32\36"+
		"(\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDF"+
		"HJL\2\5\4\2\4\4\22\25\3\2\n\r\n\2\21\21\33 ##..\64\64;;==YY\2\u0157\2"+
		"Q\3\2\2\2\4a\3\2\2\2\6g\3\2\2\2\bi\3\2\2\2\nm\3\2\2\2\fq\3\2\2\2\16u\3"+
		"\2\2\2\20\u0080\3\2\2\2\22\u0084\3\2\2\2\24\u008f\3\2\2\2\26\u0093\3\2"+
		"\2\2\30\u009e\3\2\2\2\32\u00a2\3\2\2\2\34\u00c3\3\2\2\2\36\u00c5\3\2\2"+
		"\2 \u00d3\3\2\2\2\"\u00e3\3\2\2\2$\u00e5\3\2\2\2&\u00e7\3\2\2\2(\u00e9"+
		"\3\2\2\2*\u00eb\3\2\2\2,\u00f0\3\2\2\2.\u00ff\3\2\2\2\60\u0101\3\2\2\2"+
		"\62\u0103\3\2\2\2\64\u0108\3\2\2\2\66\u0115\3\2\2\28\u0117\3\2\2\2:\u0119"+
		"\3\2\2\2<\u0126\3\2\2\2>\u0129\3\2\2\2@\u012b\3\2\2\2B\u0131\3\2\2\2D"+
		"\u0133\3\2\2\2F\u0138\3\2\2\2H\u0146\3\2\2\2J\u014f\3\2\2\2L\u0151\3\2"+
		"\2\2NP\5\4\3\2ON\3\2\2\2PS\3\2\2\2QO\3\2\2\2QR\3\2\2\2RW\3\2\2\2SQ\3\2"+
		"\2\2TV\5\6\4\2UT\3\2\2\2VY\3\2\2\2WU\3\2\2\2WX\3\2\2\2XZ\3\2\2\2YW\3\2"+
		"\2\2Z[\7\2\2\3[\3\3\2\2\2\\b\5\b\5\2]b\5\20\t\2^b\5\f\7\2_b\5\24\13\2"+
		"`b\5\30\r\2a\\\3\2\2\2a]\3\2\2\2a^\3\2\2\2a_\3\2\2\2a`\3\2\2\2b\5\3\2"+
		"\2\2ch\5 \21\2dh\5,\27\2eh\5\64\33\2fh\5F$\2gc\3\2\2\2gd\3\2\2\2ge\3\2"+
		"\2\2gf\3\2\2\2h\7\3\2\2\2ij\79\2\2jk\5\n\6\2kl\7\3\2\2l\t\3\2\2\2mn\5"+
		"\36\20\2no\7\4\2\2op\7\5\2\2p\13\3\2\2\2qr\7\6\2\2rs\5\16\b\2st\7\3\2"+
		"\2t\r\3\2\2\2uv\b\b\1\2vw\5\34\17\2w}\3\2\2\2xy\f\3\2\2yz\7\4\2\2z|\5"+
		"\34\17\2{x\3\2\2\2|\177\3\2\2\2}{\3\2\2\2}~\3\2\2\2~\17\3\2\2\2\177}\3"+
		"\2\2\2\u0080\u0081\7\7\2\2\u0081\u0082\5\22\n\2\u0082\u0083\7\3\2\2\u0083"+
		"\21\3\2\2\2\u0084\u0085\b\n\1\2\u0085\u0086\5\34\17\2\u0086\u008c\3\2"+
		"\2\2\u0087\u0088\f\3\2\2\u0088\u0089\7\4\2\2\u0089\u008b\5\34\17\2\u008a"+
		"\u0087\3\2\2\2\u008b\u008e\3\2\2\2\u008c\u008a\3\2\2\2\u008c\u008d\3\2"+
		"\2\2\u008d\23\3\2\2\2\u008e\u008c\3\2\2\2\u008f\u0090\7\b\2\2\u0090\u0091"+
		"\5\26\f\2\u0091\u0092\7\3\2\2\u0092\25\3\2\2\2\u0093\u0094\b\f\1\2\u0094"+
		"\u0095\5\34\17\2\u0095\u009b\3\2\2\2\u0096\u0097\f\3\2\2\u0097\u0098\7"+
		"\4\2\2\u0098\u009a\5\34\17\2\u0099\u0096\3\2\2\2\u009a\u009d\3\2\2\2\u009b"+
		"\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c\27\3\2\2\2\u009d\u009b\3\2\2"+
		"\2\u009e\u009f\7\t\2\2\u009f\u00a0\5\32\16\2\u00a0\u00a1\7\3\2\2\u00a1"+
		"\31\3\2\2\2\u00a2\u00a3\b\16\1\2\u00a3\u00a4\5\34\17\2\u00a4\u00aa\3\2"+
		"\2\2\u00a5\u00a6\f\3\2\2\u00a6\u00a7\7\4\2\2\u00a7\u00a9\5\34\17\2\u00a8"+
		"\u00a5\3\2\2\2\u00a9\u00ac\3\2\2\2\u00aa\u00a8\3\2\2\2\u00aa\u00ab\3\2"+
		"\2\2\u00ab\33\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ad\u00af\7X\2\2\u00ae\u00ad"+
		"\3\2\2\2\u00af\u00b2\3\2\2\2\u00b0\u00ae\3\2\2\2\u00b0\u00b1\3\2\2\2\u00b1"+
		"\u00b6\3\2\2\2\u00b2\u00b0\3\2\2\2\u00b3\u00b5\7Y\2\2\u00b4\u00b3\3\2"+
		"\2\2\u00b5\u00b8\3\2\2\2\u00b6\u00b4\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7"+
		"\u00c4\3\2\2\2\u00b8\u00b6\3\2\2\2\u00b9\u00c4\7\n\2\2\u00ba\u00c4\7\13"+
		"\2\2\u00bb\u00c4\7\f\2\2\u00bc\u00c4\7\r\2\2\u00bd\u00c4\7\16\2\2\u00be"+
		"\u00c4\7\17\2\2\u00bf\u00c4\7\20\2\2\u00c0\u00c4\7\5\2\2\u00c1\u00c4\7"+
		"\7\2\2\u00c2\u00c4\7\21\2\2\u00c3\u00b0\3\2\2\2\u00c3\u00b9\3\2\2\2\u00c3"+
		"\u00ba\3\2\2\2\u00c3\u00bb\3\2\2\2\u00c3\u00bc\3\2\2\2\u00c3\u00bd\3\2"+
		"\2\2\u00c3\u00be\3\2\2\2\u00c3\u00bf\3\2\2\2\u00c3\u00c0\3\2\2\2\u00c3"+
		"\u00c1\3\2\2\2\u00c3\u00c2\3\2\2\2\u00c4\35\3\2\2\2\u00c5\u00c6\b\20\1"+
		"\2\u00c6\u00c7\5\34\17\2\u00c7\u00cd\3\2\2\2\u00c8\u00c9\f\3\2\2\u00c9"+
		"\u00ca\t\2\2\2\u00ca\u00cc\5\34\17\2\u00cb\u00c8\3\2\2\2\u00cc\u00cf\3"+
		"\2\2\2\u00cd\u00cb\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\37\3\2\2\2\u00cf"+
		"\u00cd\3\2\2\2\u00d0\u00d2\5*\26\2\u00d1\u00d0\3\2\2\2\u00d2\u00d5\3\2"+
		"\2\2\u00d3\u00d1\3\2\2\2\u00d3\u00d4\3\2\2\2\u00d4\u00d6\3\2\2\2\u00d5"+
		"\u00d3\3\2\2\2\u00d6\u00d7\5\"\22\2\u00d7\u00d8\5$\23\2\u00d8\u00d9\5"+
		"&\24\2\u00d9\u00da\5(\25\2\u00da\u00de\7\26\2\2\u00db\u00dd\5:\36\2\u00dc"+
		"\u00db\3\2\2\2\u00dd\u00e0\3\2\2\2\u00de\u00dc\3\2\2\2\u00de\u00df\3\2"+
		"\2\2\u00df\u00e1\3\2\2\2\u00e0\u00de\3\2\2\2\u00e1\u00e2\7\27\2\2\u00e2"+
		"!\3\2\2\2\u00e3\u00e4\7\17\2\2\u00e4#\3\2\2\2\u00e5\u00e6\t\3\2\2\u00e6"+
		"%\3\2\2\2\u00e7\u00e8\7Y\2\2\u00e8\'\3\2\2\2\u00e9\u00ea\7X\2\2\u00ea"+
		")\3\2\2\2\u00eb\u00ec\7\\\2\2\u00ec+\3\2\2\2\u00ed\u00ef\5*\26\2\u00ee"+
		"\u00ed\3\2\2\2\u00ef\u00f2\3\2\2\2\u00f0\u00ee\3\2\2\2\u00f0\u00f1\3\2"+
		"\2\2\u00f1\u00f3\3\2\2\2\u00f2\u00f0\3\2\2\2\u00f3\u00f4\5.\30\2\u00f4"+
		"\u00f5\5\60\31\2\u00f5\u00f6\5\62\32\2\u00f6\u00fa\7\26\2\2\u00f7\u00f9"+
		"\5:\36\2\u00f8\u00f7\3\2\2\2\u00f9\u00fc\3\2\2\2\u00fa\u00f8\3\2\2\2\u00fa"+
		"\u00fb\3\2\2\2\u00fb\u00fd\3\2\2\2\u00fc\u00fa\3\2\2\2\u00fd\u00fe\7\27"+
		"\2\2\u00fe-\3\2\2\2\u00ff\u0100\7\20\2\2\u0100/\3\2\2\2\u0101\u0102\7"+
		"Y\2\2\u0102\61\3\2\2\2\u0103\u0104\7X\2\2\u0104\63\3\2\2\2\u0105\u0107"+
		"\5*\26\2\u0106\u0105\3\2\2\2\u0107\u010a\3\2\2\2\u0108\u0106\3\2\2\2\u0108"+
		"\u0109\3\2\2\2\u0109\u010b\3\2\2\2\u010a\u0108\3\2\2\2\u010b\u010c\5\66"+
		"\34\2\u010c\u010d\58\35\2\u010d\u010f\7\26\2\2\u010e\u0110\5:\36\2\u010f"+
		"\u010e\3\2\2\2\u0110\u0111\3\2\2\2\u0111\u010f\3\2\2\2\u0111\u0112\3\2"+
		"\2\2\u0112\u0113\3\2\2\2\u0113\u0114\7\27\2\2\u0114\65\3\2\2\2\u0115\u0116"+
		"\7\16\2\2\u0116\67\3\2\2\2\u0117\u0118\7Y\2\2\u01189\3\2\2\2\u0119\u011b"+
		"\5@!\2\u011a\u011c\5<\37\2\u011b\u011a\3\2\2\2\u011b\u011c\3\2\2\2\u011c"+
		"\u011d\3\2\2\2\u011d\u0120\5B\"\2\u011e\u011f\7\30\2\2\u011f\u0121\5>"+
		" \2\u0120\u011e\3\2\2\2\u0120\u0121\3\2\2\2\u0121\u0122\3\2\2\2\u0122"+
		"\u0124\7\3\2\2\u0123\u0125\5D#\2\u0124\u0123\3\2\2\2\u0124\u0125\3\2\2"+
		"\2\u0125;\3\2\2\2\u0126\u0127\7\31\2\2\u0127\u0128\7\32\2\2\u0128=\3\2"+
		"\2\2\u0129\u012a\7X\2\2\u012a?\3\2\2\2\u012b\u012c\t\4\2\2\u012cA\3\2"+
		"\2\2\u012d\u0132\7Y\2\2\u012e\u0132\5\"\22\2\u012f\u0132\5\66\34\2\u0130"+
		"\u0132\5.\30\2\u0131\u012d\3\2\2\2\u0131\u012e\3\2\2\2\u0131\u012f\3\2"+
		"\2\2\u0131\u0130\3\2\2\2\u0132C\3\2\2\2\u0133\u0134\7\\\2\2\u0134E\3\2"+
		"\2\2\u0135\u0137\5*\26\2\u0136\u0135\3\2\2\2\u0137\u013a\3\2\2\2\u0138"+
		"\u0136\3\2\2\2\u0138\u0139\3\2\2\2\u0139\u013b\3\2\2\2\u013a\u0138\3\2"+
		"\2\2\u013b\u013c\5J&\2\u013c\u013d\5L\'\2\u013d\u013e\7\26\2\2\u013e\u0140"+
		"\5H%\2\u013f\u0141\5H%\2\u0140\u013f\3\2\2\2\u0141\u0142\3\2\2\2\u0142"+
		"\u0140\3\2\2\2\u0142\u0143\3\2\2\2\u0143\u0144\3\2\2\2\u0144\u0145\7\27"+
		"\2\2\u0145G\3\2\2\2\u0146\u0149\5B\"\2\u0147\u0148\7\30\2\2\u0148\u014a"+
		"\5> \2\u0149\u0147\3\2\2\2\u0149\u014a\3\2\2\2\u014a\u014b\3\2\2\2\u014b"+
		"\u014d\7\3\2\2\u014c\u014e\5D#\2\u014d\u014c\3\2\2\2\u014d\u014e\3\2\2"+
		"\2\u014eI\3\2\2\2\u014f\u0150\7\60\2\2\u0150K\3\2\2\2\u0151\u0152\7Y\2"+
		"\2\u0152M\3\2\2\2\34QWag}\u008c\u009b\u00aa\u00b0\u00b6\u00c3\u00cd\u00d3"+
		"\u00de\u00f0\u00fa\u0108\u0111\u011b\u0120\u0124\u0131\u0138\u0142\u0149"+
		"\u014d";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}