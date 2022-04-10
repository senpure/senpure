// Generated from E:/Projects/senpure/senpure-io/senpure-io-generator/src/main/resources\Io.g4 by ANTLR 4.7.2
package com.senpure.io.antlr;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link IoParser}.
 */
public interface IoListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link IoParser#protocol}.
	 * @param ctx the parse tree
	 */
	void enterProtocol(IoParser.ProtocolContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#protocol}.
	 * @param ctx the parse tree
	 */
	void exitProtocol(IoParser.ProtocolContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#headContent}.
	 * @param ctx the parse tree
	 */
	void enterHeadContent(IoParser.HeadContentContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#headContent}.
	 * @param ctx the parse tree
	 */
	void exitHeadContent(IoParser.HeadContentContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#entity}.
	 * @param ctx the parse tree
	 */
	void enterEntity(IoParser.EntityContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#entity}.
	 * @param ctx the parse tree
	 */
	void exitEntity(IoParser.EntityContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#importIo}.
	 * @param ctx the parse tree
	 */
	void enterImportIo(IoParser.ImportIoContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#importIo}.
	 * @param ctx the parse tree
	 */
	void exitImportIo(IoParser.ImportIoContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#importValue}.
	 * @param ctx the parse tree
	 */
	void enterImportValue(IoParser.ImportValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#importValue}.
	 * @param ctx the parse tree
	 */
	void exitImportValue(IoParser.ImportValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#javaPackage}.
	 * @param ctx the parse tree
	 */
	void enterJavaPackage(IoParser.JavaPackageContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#javaPackage}.
	 * @param ctx the parse tree
	 */
	void exitJavaPackage(IoParser.JavaPackageContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#javaPackageValue}.
	 * @param ctx the parse tree
	 */
	void enterJavaPackageValue(IoParser.JavaPackageValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#javaPackageValue}.
	 * @param ctx the parse tree
	 */
	void exitJavaPackageValue(IoParser.JavaPackageValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#namespace}.
	 * @param ctx the parse tree
	 */
	void enterNamespace(IoParser.NamespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#namespace}.
	 * @param ctx the parse tree
	 */
	void exitNamespace(IoParser.NamespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#namespaceValue}.
	 * @param ctx the parse tree
	 */
	void enterNamespaceValue(IoParser.NamespaceValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#namespaceValue}.
	 * @param ctx the parse tree
	 */
	void exitNamespaceValue(IoParser.NamespaceValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#luaNamespace}.
	 * @param ctx the parse tree
	 */
	void enterLuaNamespace(IoParser.LuaNamespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#luaNamespace}.
	 * @param ctx the parse tree
	 */
	void exitLuaNamespace(IoParser.LuaNamespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#luaNamespaceValue}.
	 * @param ctx the parse tree
	 */
	void enterLuaNamespaceValue(IoParser.LuaNamespaceValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#luaNamespaceValue}.
	 * @param ctx the parse tree
	 */
	void exitLuaNamespaceValue(IoParser.LuaNamespaceValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#jsNamespace}.
	 * @param ctx the parse tree
	 */
	void enterJsNamespace(IoParser.JsNamespaceContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#jsNamespace}.
	 * @param ctx the parse tree
	 */
	void exitJsNamespace(IoParser.JsNamespaceContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#jsNamespaceValue}.
	 * @param ctx the parse tree
	 */
	void enterJsNamespaceValue(IoParser.JsNamespaceValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#jsNamespaceValue}.
	 * @param ctx the parse tree
	 */
	void exitJsNamespaceValue(IoParser.JsNamespaceValueContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#fileName}.
	 * @param ctx the parse tree
	 */
	void enterFileName(IoParser.FileNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#fileName}.
	 * @param ctx the parse tree
	 */
	void exitFileName(IoParser.FileNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#filePath}.
	 * @param ctx the parse tree
	 */
	void enterFilePath(IoParser.FilePathContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#filePath}.
	 * @param ctx the parse tree
	 */
	void exitFilePath(IoParser.FilePathContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#message}.
	 * @param ctx the parse tree
	 */
	void enterMessage(IoParser.MessageContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#message}.
	 * @param ctx the parse tree
	 */
	void exitMessage(IoParser.MessageContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#messageHead}.
	 * @param ctx the parse tree
	 */
	void enterMessageHead(IoParser.MessageHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#messageHead}.
	 * @param ctx the parse tree
	 */
	void exitMessageHead(IoParser.MessageHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#messageType}.
	 * @param ctx the parse tree
	 */
	void enterMessageType(IoParser.MessageTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#messageType}.
	 * @param ctx the parse tree
	 */
	void exitMessageType(IoParser.MessageTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#messageName}.
	 * @param ctx the parse tree
	 */
	void enterMessageName(IoParser.MessageNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#messageName}.
	 * @param ctx the parse tree
	 */
	void exitMessageName(IoParser.MessageNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#messageId}.
	 * @param ctx the parse tree
	 */
	void enterMessageId(IoParser.MessageIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#messageId}.
	 * @param ctx the parse tree
	 */
	void exitMessageId(IoParser.MessageIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#entityComment}.
	 * @param ctx the parse tree
	 */
	void enterEntityComment(IoParser.EntityCommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#entityComment}.
	 * @param ctx the parse tree
	 */
	void exitEntityComment(IoParser.EntityCommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#event}.
	 * @param ctx the parse tree
	 */
	void enterEvent(IoParser.EventContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#event}.
	 * @param ctx the parse tree
	 */
	void exitEvent(IoParser.EventContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#eventHead}.
	 * @param ctx the parse tree
	 */
	void enterEventHead(IoParser.EventHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#eventHead}.
	 * @param ctx the parse tree
	 */
	void exitEventHead(IoParser.EventHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#eventName}.
	 * @param ctx the parse tree
	 */
	void enterEventName(IoParser.EventNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#eventName}.
	 * @param ctx the parse tree
	 */
	void exitEventName(IoParser.EventNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#eventId}.
	 * @param ctx the parse tree
	 */
	void enterEventId(IoParser.EventIdContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#eventId}.
	 * @param ctx the parse tree
	 */
	void exitEventId(IoParser.EventIdContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#bean}.
	 * @param ctx the parse tree
	 */
	void enterBean(IoParser.BeanContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#bean}.
	 * @param ctx the parse tree
	 */
	void exitBean(IoParser.BeanContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#beanHead}.
	 * @param ctx the parse tree
	 */
	void enterBeanHead(IoParser.BeanHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#beanHead}.
	 * @param ctx the parse tree
	 */
	void exitBeanHead(IoParser.BeanHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#beanName}.
	 * @param ctx the parse tree
	 */
	void enterBeanName(IoParser.BeanNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#beanName}.
	 * @param ctx the parse tree
	 */
	void exitBeanName(IoParser.BeanNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#field}.
	 * @param ctx the parse tree
	 */
	void enterField(IoParser.FieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#field}.
	 * @param ctx the parse tree
	 */
	void exitField(IoParser.FieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#fieldList}.
	 * @param ctx the parse tree
	 */
	void enterFieldList(IoParser.FieldListContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#fieldList}.
	 * @param ctx the parse tree
	 */
	void exitFieldList(IoParser.FieldListContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#fieldIndex}.
	 * @param ctx the parse tree
	 */
	void enterFieldIndex(IoParser.FieldIndexContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#fieldIndex}.
	 * @param ctx the parse tree
	 */
	void exitFieldIndex(IoParser.FieldIndexContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#fieldType}.
	 * @param ctx the parse tree
	 */
	void enterFieldType(IoParser.FieldTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#fieldType}.
	 * @param ctx the parse tree
	 */
	void exitFieldType(IoParser.FieldTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#fieldName}.
	 * @param ctx the parse tree
	 */
	void enterFieldName(IoParser.FieldNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#fieldName}.
	 * @param ctx the parse tree
	 */
	void exitFieldName(IoParser.FieldNameContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#fieldComment}.
	 * @param ctx the parse tree
	 */
	void enterFieldComment(IoParser.FieldCommentContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#fieldComment}.
	 * @param ctx the parse tree
	 */
	void exitFieldComment(IoParser.FieldCommentContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#enumSymbol}.
	 * @param ctx the parse tree
	 */
	void enterEnumSymbol(IoParser.EnumSymbolContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#enumSymbol}.
	 * @param ctx the parse tree
	 */
	void exitEnumSymbol(IoParser.EnumSymbolContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#enumField}.
	 * @param ctx the parse tree
	 */
	void enterEnumField(IoParser.EnumFieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#enumField}.
	 * @param ctx the parse tree
	 */
	void exitEnumField(IoParser.EnumFieldContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#enumHead}.
	 * @param ctx the parse tree
	 */
	void enterEnumHead(IoParser.EnumHeadContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#enumHead}.
	 * @param ctx the parse tree
	 */
	void exitEnumHead(IoParser.EnumHeadContext ctx);
	/**
	 * Enter a parse tree produced by {@link IoParser#enumName}.
	 * @param ctx the parse tree
	 */
	void enterEnumName(IoParser.EnumNameContext ctx);
	/**
	 * Exit a parse tree produced by {@link IoParser#enumName}.
	 * @param ctx the parse tree
	 */
	void exitEnumName(IoParser.EnumNameContext ctx);
}