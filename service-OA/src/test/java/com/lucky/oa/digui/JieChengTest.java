package com.lucky.oa.digui;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.expression.spel.ast.OpAnd;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lucky
 * @create 2023-04-18 14:09
 */

public class JieChengTest {

    public static void main(String[] args) {
        JieChengTest main = new JieChengTest();
        /**
         *
         *
         *     *           *            *
         *    **           *            *
         *   ***           *            *
         *  ********     ********     ********
         *
         *
         */
        List<Integer> begin = new ArrayList<>();
        List<Integer> temp = new ArrayList<>();
        List<Integer> end = new ArrayList<>();
        for(int i = 3; i > 0 ;i--){
            begin.add(i);
        }

//        int result = main.carry(1,begin,temp,end);
//        System.out.println(result);

    }


    public void carry(
            Integer num,
            List<Integer> begin,
            List<Integer> temp,
            List<Integer> end
    ){



    }



}
