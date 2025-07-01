public class SimpleHandler {
    //binary to integer
    public int BTI(int[] bin) {
        StringBuilder binStr = new StringBuilder();
        for(int bit : bin) {
            binStr.append(bit);
        }
        return Integer.parseInt(binStr.toString(), 2);
    }
    //hexadecimal to integer
    public int HTI(String hex) {
        return Integer.parseInt(hex, 16);
    }
    //octal to integer
    public int OTI(String oct){
        return Integer.parseInt(oct, 8);
    }
    private int[] hexToBinArr(String hex, int len) {
        int deciVal = Integer.parseInt(hex, 16);
        String binStr = String.format("%" + len + "s", Integer.toBinaryString(deciVal)).replace(' ', '0');
        int[] binArr = new int[len];
        for (int i = 0; i < len; i++) {
            binArr[i] = Character.getNumericValue(binStr.charAt(i));
        }
        return binArr;
    }
    //octal to decimal
    int[] octTobinArr(String octal, int len) {
        int deciVal = Integer.parseInt(octal, 8);
        String binStr = String.format("%" + len + "s", Integer.toBinaryString(deciVal)).replace(' ', '0');
        int[] binArr = new int[len];
        for (int i = 0; i < len; i++) {
            binArr[i] = Character.getNumericValue(binStr.charAt(i));
        }
        return binArr;
    }
    public int[] hexToBinArr(String hex) {
        return hexToBinArr(hex, 16);
    }
    public int[] hexToBinArrSh(String hex) {
        return hexToBinArr(hex, 12);
    }
    public int[] intTobinArr(int value) {
        int[] binArr = new int[16];
        for (int i = 15; i >= 0; i--) {
            binArr[i] = (value & 1);
            value >>= 1;
        }
        return binArr;
    }
}