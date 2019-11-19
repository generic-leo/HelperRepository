package com.leo.structure2019.main.helpers;

public class ArgumentData {
    private String textValue = "Nothing";
    private Integer intValue = Integer.MIN_VALUE;

    public ArgumentData(String textValue){
        this.textValue = textValue;
    }

    public ArgumentData(int intValue){
        this.intValue = intValue;
    }

    public String getText(){
        return textValue;
    }

    public int getInt(){
        return intValue;
    }

    public Class getType(){
        if(!textValue.equalsIgnoreCase("Nothing")){
            return String.class;
        }

        return Integer.class;
    }
}
