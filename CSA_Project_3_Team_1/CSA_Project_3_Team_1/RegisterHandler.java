import java.util.Arrays;
public class RegisterHandler {
    private int[] reVal;
    private int reSiz;
    public RegisterHandler(int s){
        inReSiz(s);
    }
    public int[] getRegVal(){
        return reVal;
    }
    public void setRegVal(int[] n){
        if (reSiz == 0){
            throw new IllegalStateException("Register size found to be zero. You need to enter data in order to set value. Please Try Again");
        }
        this.reVal = Arrays.copyOf(n, n.length);
    }
    private void inReSiz(int SIZE){
        this.reSiz = SIZE;
        this.reVal = new int[SIZE];
    }
}