package com.llj.commondemo.core.router;

import android.app.Activity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明当前 Fragment 被哪个 Activity 启动
 *
 * Inherited：如果一个使用了@Inherited修饰的annotation类型被用于一个class，
 * 则这个annotation将被用于该class的子类。
 */

@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AttachedActivity {

    /**
     * 要打开 Activity 的类名
     *
     * @return
     */
    Class<? extends Activity> value();
}
