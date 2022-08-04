package me.asakura_kukii.siegefishing.utility.argument;

public class Argument {
    int current = 0;
    private String[] args;

    public Argument(String[] args){
        this.args = args;
    }

    public static boolean completeString(String arg, String startsWith) {
        if (arg == null || startsWith == null)
            return false;
        return arg.toLowerCase().startsWith(startsWith.toLowerCase());
    }

    public boolean hasNext(){
        return current < args.length;
    }

    public Integer nextInt(){
        try{
            String arg = args[current++];
            return Integer.parseInt(arg);
        }catch (Exception e){
            current --;
            return null;
        }
    }

    public Double nextDouble(){
        try{
            String arg = args[current++];
            return Double.parseDouble(arg);
        }catch (Exception e){
            current --;
            return null;
        }
    }

    public Long nextLong(){
        try{
            String arg = args[current++];
            return Long.parseLong(arg);
        }catch (Exception e){
            current --;
            return null;
        }
    }

    public Boolean nextBoolean(){
        try{
            String arg = args[current++];
            return Boolean.parseBoolean(arg);
        }catch (Exception e){
            current --;
            return null;
        }
    }

    public String nextString(){
        try{
            String arg = args[current++];
            return arg;
        }catch (Exception e){
            current --;
            return null;
        }
    }

    public String peek() {
        return args[current];
    }
}
