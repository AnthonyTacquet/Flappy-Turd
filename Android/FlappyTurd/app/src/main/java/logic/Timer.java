package logic;

public class Timer {
    int mili;
    boolean finished = false;
    Thread thread;

    public Timer(){
        this.mili = 0;
    }
    public Timer(int mili){
        this.mili = mili;
    }

    //public boolean isFinished(){
     //   return this.finished;
    //}

    public int getMili(){
        return this.mili;
    }

    public void setMili(int mili){
        this.mili = mili;
    }

    public Thread getThread(){
        return this.thread;
    }

    public void startTimer(){
        thread = new Thread(() -> {
            try {
                Thread.sleep(mili);
                //finished = true;
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        });
        thread.start();
    }

    public void resetTimer(){
        //this.finished = false;
        startTimer();
    }
}
