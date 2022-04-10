grammar Io;
protocol
    :
    headContent*
    entity*EOF
    ;
headContent:importIo|namespace|javaPackage|luaNamespace|jsNamespace;
entity:message|event|bean|enumSymbol;
importIo:'import'importValue';';
importValue: filePath'.''io';

javaPackage:'javaPackage'javaPackageValue';';
javaPackageValue
	:fileName
	|javaPackageValue '.'fileName
	;
namespace:'namespace'namespaceValue ';';
namespaceValue
    :fileName
    |namespaceValue '.'fileName
    ;
luaNamespace:'luaNamespace'luaNamespaceValue ';';
luaNamespaceValue
     :fileName
     |luaNamespaceValue '.'fileName
     ;
jsNamespace:'jsNamespace'jsNamespaceValue ';';
jsNamespaceValue
     :fileName
     |jsNamespaceValue '.'fileName
     ;
fileName:(Number* Identifier*)|'cs'|'CS'|'sc'|'SC'|'bean'|'message'|'event'|'io'|'namespace';
filePath:fileName|filePath ('-'|'.'|'../'|'/'|'\\') fileName;


message
    :entityComment*
    messageHead messageType messageName messageId '{'
     field*
    '}';
messageHead:'message';
messageType:'cs'|'CS'|'sc'|'SC';
messageName:Identifier;
messageId:Number;
entityComment:LINE_COMMENT;
event
    :entityComment*
    eventHead  eventName eventId '{'
     field*
   '}'
   ;
 eventHead:'event';
 eventName:Identifier;
 eventId:Number;

bean
    :entityComment*
    beanHead beanName'{'
     field+//至少需要一个字段才有必要定义bean
    '}'
    ;
beanHead:'bean';
beanName:Identifier;
field
    :fieldType fieldList? fieldName ('='fieldIndex)?';'fieldComment?
    ;
fieldList:'['']';
fieldIndex:Number;
fieldType
    :'int'
    |'long'
    |'sint'
    |'slong'
    |'fixed32'
    |'fixed64'
    |'float'
    |'double'
    |'boolean'
    |'String'
    |'string'
    |'bytes'
    |Identifier
    ;
fieldName:Identifier|messageHead|beanHead|eventHead;
fieldComment:LINE_COMMENT;
enumSymbol  //至少需要两个状态才有必要定义枚举,强制第一个位默认值
    :entityComment*
     enumHead enumName'{'
     enumField
     enumField+
    '}'
    ;
enumField:fieldName('='fieldIndex)?';'fieldComment?;
enumHead:'enum';
enumName:Identifier;
//↓↓java keywords↓↓
ABSTRACT : 'abstract';
ASSERT : 'assert';
BOOLEAN : 'boolean';
BREAK : 'break';
BYTE : 'byte';
CASE : 'case';
CATCH : 'catch';
CHAR : 'char';
CLASS : 'class';
CONST : 'const';
CONTINUE : 'continue';
DEFAULT : 'default';
DO : 'do';
DOUBLE : 'double';
ELSE : 'else';
ENUM : 'enum';
EXTENDS : 'extends';
FINAL : 'final';
FINALLY : 'finally';
FLOAT : 'float';
FOR : 'for';
IF : 'if';
GOTO : 'goto';
IMPLEMENTS : 'implements';
IMPORT : 'import';
INSTANCEOF : 'instanceof';
INT : 'int';
INTERFACE : 'interface';
LONG : 'long';
NATIVE : 'native';
NEW : 'new';
PACKAGE : 'package';
PRIVATE : 'private';
PROTECTED : 'protected';
PUBLIC : 'public';
RETURN : 'return';
SHORT : 'short';
STATIC : 'static';
STRICTFP : 'strictfp';
SUPER : 'super';
SWITCH : 'switch';
SYNCHRONIZED : 'synchronized';
THIS : 'this';
THROW : 'throw';
THROWS : 'throws';
TRANSIENT : 'transient';
TRY : 'try';
VOID : 'void';
VOLATILE : 'volatile';
WHILE : 'while';
//↑↑java keywords↑↑

//↓↓lua keywords↓↓
SELF:'self';
//↑↑lua keywords↑↑

//↓↓protocol keywords↓↓
MESSAGEID:'messageId';
EVENTID:'eventId';
SERIALIZEDSIZE:'serializedSize';
UNKNOWN:'UNKNOWN';

//↑↑protocol keywords↑↑
Number:[0-9]+;
fragment Letter:	[a-zA-Z_] ;
fragment LetterOrDigit:	[a-zA-Z0-9_] ;
fragment DIGIT : [0-9];
Identifier:	Letter LetterOrDigit*  	;
//WS      :  [ \t\r\n\u000C]+ -> skip ;
WS      :  [ \t\r\n]+ -> skip ;
COMMENT :   '/*' .*? '*/' -> skip;
LINE_COMMENT:   '//' ~[\r\n]*;
CODE_COMMENT:   '#' ~[\r\n]* -> skip ;
/*
LINE_COMMENT
    :'//' .*? '\r'? '\n'//匹配//于换行中间的所有字符
    ;
*/