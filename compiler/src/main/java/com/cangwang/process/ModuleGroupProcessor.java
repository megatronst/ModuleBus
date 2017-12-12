package com.cangwang.process;

import com.cangwang.annotation.ModuleGroup;
import com.cangwang.annotation.ModuleUnit;
import com.cangwang.utils.Logger;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.javapoet.ClassName;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * 解析ModuleGroup注解
 */
public class ModuleGroupProcessor  {

    public static JsonArray parseModulesGroup(Set<? extends Element> modulesElements, Logger logger, Filer mFiler, Elements elements) throws IOException{
        if (CollectionUtils.isNotEmpty(modulesElements)) {
            logger.info(">>> Found moduleGroup, size is " + modulesElements.size() + " <<<");
            for (Element element:modulesElements){
                if (element!=null){
                    ModuleGroup group = element.getAnnotation(ModuleGroup.class);
                    if (group!=null){
                        ModuleUnit[] units = group.value();
                         return parseModules(units,element,logger,mFiler,elements);
                    }
                }
            }
        }
        return null;
    }

    public static JsonArray parseModules(ModuleUnit[] modulesElements,Element element,Logger logger,Filer mFiler,Elements elements) throws IOException {
        if (modulesElements.length > 0){
            logger.info(">>> Found moduleUnit, size is " + modulesElements.length + " <<<");

            ModuleUnit moduleUnit;
            JsonArray array = new JsonArray();
            for (int i = 0;i<modulesElements.length;i++) {
                moduleUnit=modulesElements[i];
                ClassName name = ClassName.get(((TypeElement)element));
                String address = name.packageName()+"."+name.simpleName();  //真实模块入口地址 包名+类名
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("path",address);
                jsonObject.addProperty("templet",moduleUnit.templet());
                jsonObject.addProperty("title",moduleUnit.title());
                jsonObject.addProperty("layout_level",moduleUnit.layoutlevel().getValue());
                jsonObject.addProperty("extra_level",moduleUnit.extralevel());
                array.add(jsonObject);
            }
            logger.info(array.toString());
            return array;
        }
        return null;
    }
}
