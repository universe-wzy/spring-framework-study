package com.wzy.springframework;

import org.aspectj.lang.reflect.SourceLocation;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

public aspect AspectDemo {
    //声明一个pointcut，匹配你需要的方法
    pointcut onMouseClicked(MouseEvent e) :
            execution(* mouseClicked(MouseEvent)) && args(e);
    pointcut onMousePressed(MouseEvent e) :
            execution(* mousePressed(MouseEvent)) && args(e);
    pointcut onMouseReleased(MouseEvent e) :
            execution(* mouseReleased(MouseEvent)) && args(e);
    pointcut onActionPerformed(ActionEvent e) :
            execution(* actionPerformed(ActionEvent)) && args(e);

    //before表示之前的意思
    //这整个表示在 mouseXXX(MouseEvent) 方法调用之前，你想要执行的代码
    before(MouseEvent e) : onMouseClicked(e) || onMousePressed(e) || onMouseReleased(e) {
        SourceLocation sl = thisJoinPoint.getSourceLocation();//切面对应的代码位置
        System.out.println(sl);
        System.out.println(e);
        System.out.println(e.getSource());
    }
    //同上
    before(ActionEvent e) : onActionPerformed(e) { // && !within(LogHandlerBar) {
        SourceLocation sl = thisJoinPoint.getSourceLocation();
        System.out.println(sl);
        System.out.println(e);
        System.out.println(e.getSource());
    }
}
