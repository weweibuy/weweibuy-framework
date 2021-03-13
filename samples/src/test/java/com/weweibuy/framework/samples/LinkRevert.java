package com.weweibuy.framework.samples;

import lombok.Data;
import org.junit.Test;

/**
 * @author durenhao
 * @date 2021/3/3 20:57
 **/
public class LinkRevert {


    @Test
    public void test() {
        Node<Integer> integerNode = new Node<>();
        integerNode.setValue(1);

        Node<Integer> integerNode2 = new Node<>();
        integerNode2.setValue(2);

        Node<Integer> integerNode3 = new Node<>();
        integerNode3.setValue(3);

        integerNode.setNext(integerNode2);
        integerNode2.setNext(integerNode3);

        integerNode.revert();
        String s = "";
    }

    @Data
    private static class Node<T> {

        private T value;

        private Node<T> next;


        public void revert(){
            if(next != null){
                next.setNext(this);
                next.revert();
            }

        }


    }


}
