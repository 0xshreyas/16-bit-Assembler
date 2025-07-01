import java.util.Arrays;
public class SimpleMemory {
    private final int[][] MEMVAL;
    public SimpleMemory(){ //Implementing Memory
        this.MEMVAL = new int[2048][16]; //2048 words for every 16 bits
    }
    public int[] getMemVal(int row) {
        return Arrays.copyOfRange(this.MEMVAL[row],0,16);
    }
    public void setMemVal(int row, int[] val) {
        System.arraycopy(val, 0, this.MEMVAL[row], 0, 16);
    }
}