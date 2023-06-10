package me.zhenxin.zmusic.api;

public class ProgressBar {
    final int length = 25;
    char finish;
    char unFinish;
    double progress;
    double maxnumber = 100;

    public ProgressBar(char finish, char unFinish) {
        this.finish = finish;
        this.unFinish = unFinish;
    }

    public ProgressBar(char finish, char unFinish, long maxnumber) {
        this.finish = finish;
        this.unFinish = unFinish;
        this.maxnumber = maxnumber;
    }

    public void setMaxnumber(double maxnumber) {
        this.maxnumber = maxnumber;
    }

    public char getFinish() {
        return finish;
    }

    public void setFinish(char finish) {
        this.finish = finish;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        if (progress < 0 && progress > maxnumber) {
            throw new ArithmeticException();
        }
        this.progress = progress;
    }

    public char getUnFinish() {
        return unFinish;
    }

    public void setUnFinish(char unFinish) {
        this.unFinish = unFinish;
    }

    @Override
    public String toString() {
        double f = maxnumber / length;
        int prog;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        if (getProgress() != 0) {
            prog = (int) ((int) getProgress() / f);
        } else prog = 0;
        for (int i = 0; i < prog; i++) {
            stringBuilder.append(getFinish());
        }
        for (int i = 0; i < length - prog; i++) {
            stringBuilder.append(getUnFinish());
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public String getString() {
        return toString();
    }
}
