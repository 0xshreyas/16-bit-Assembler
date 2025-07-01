import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class opCodes {
    SimpleHandler Handler = new SimpleHandler();
    RegisterHandler pc = new RegisterHandler(12);
    RegisterHandler cc = new RegisterHandler(4);
    RegisterHandler ir = new RegisterHandler(16);
    RegisterHandler mar = new RegisterHandler(12);
    RegisterHandler mbr = new RegisterHandler(16);
    RegisterHandler mfr = new RegisterHandler(4);
    RegisterHandler ix1 = new RegisterHandler(16);
    RegisterHandler ix2 = new RegisterHandler(16);
    RegisterHandler ix3 = new RegisterHandler(16);
    RegisterHandler gpr0 = new RegisterHandler(16);
    RegisterHandler gpr1 = new RegisterHandler(16);
    RegisterHandler gpr2 = new RegisterHandler(16);
    RegisterHandler gpr3 = new RegisterHandler(16);
    RegisterHandler hlt = new RegisterHandler(1);
    public boolean filestat = false;
    public boolean filestat2 = false;
    SimpleMemory MAIN_MEM = new SimpleMemory();
    public void exe(String x) {
        if ("single".equals(x)) {
            int[] inst_add = getRegVal("PC");
            int int_inst_add = Handler.BTI(inst_add);
            setRegVal("IR", getMemVal(int_inst_add));
            int[] currPC = getRegVal("PC");
            int intPC = Handler.BTI(currPC);
            intPC = intPC + 1;
            int[] new_PC = intToBinArrSh(Integer.toBinaryString(intPC)); 
            setRegVal("PC", new_PC);
            int[] binInst = getMemVal(int_inst_add);
            int[] opCode = Arrays.copyOfRange(binInst, 0, 6);
            String inst = decOpCode(opCode);
            if ("LDR".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int I = res[1];
                int R = res[2];
                int IX = res[3];
                int Addr = res[4];
                setRegVal("MAR", intToBinArrSh(Integer.toBinaryString(EA))); 
                switch (R) {
                    case 0:
                        setRegVal("MBR", getMemVal(EA));
                        gpr0.setRegVal(mbr.getRegVal());
                        break;
                    case 1:
                        setRegVal("MBR", getMemVal(EA));
                        gpr1.setRegVal(mbr.getRegVal());
                        break;
                    case 2:
                        setRegVal("MBR", getMemVal(EA));
                        gpr2.setRegVal(mbr.getRegVal());
                        break;
                    default:
                        setRegVal("MBR", getMemVal(EA));
                        gpr3.setRegVal(mbr.getRegVal());
                }
                setRegVal("MBR", getMemVal(EA));
            } 
            else if ("STR".equals(inst)) {
                int[] res = comp(binInst); 
                int EA = res[0];
                int I = res[1];
                int R = res[2];
                int IX = res[3];
                int Addr = res[4];
                setRegVal("MAR", intToBinArrSh(Integer.toBinaryString(EA))); 
                switch (R) {
                    case 0:
                        setRegVal("MBR", gpr0.getRegVal());
                        setMemVal(EA, mbr.getRegVal()); 
                        break;
                    case 1:
                        setRegVal("MBR", gpr1.getRegVal());
                        setMemVal(EA, mbr.getRegVal()); 
                        break;
                    case 2:
                        setRegVal("MBR", gpr2.getRegVal());
                        setMemVal(EA, mbr.getRegVal()); 
                        break;
                    default:
                        setRegVal("MBR", gpr3.getRegVal());
                        setMemVal(EA, mbr.getRegVal()); 
                }
            } 
            else if ("LDA".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int I = res[1];
                int R = res[2];
                int IX = res[3];
                int Addr = res[4];
                setRegVal("MAR", intToBinArrSh(Integer.toBinaryString(EA))); 
                int[] val = intToBinArr(Integer.toBinaryString(EA)); 
                switch (R) {
                    case 0:
                        gpr0.setRegVal(val);
                        break;
                    case 1:
                        gpr1.setRegVal(val);
                        break;
                    case 2:
                        gpr2.setRegVal(val);
                        break;
                    default:
                        gpr3.setRegVal(val);
                }
            } 
            else if ("LDX".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int I = res[1];
                int R = res[2];
                int IX = res[3];
                int Addr = res[4];
                setRegVal("MAR", intToBinArrSh(Integer.toBinaryString(EA))); 
                switch (IX) {
                    case 1:
                        setRegVal("MBR", getMemVal(EA));
                        ix1.setRegVal(mbr.getRegVal());
                        break;
                    case 2:
                        setRegVal("MBR", getMemVal(EA));
                        ix2.setRegVal(mbr.getRegVal());
                        break;
                    case 3:
                        setRegVal("MBR", getMemVal(EA));
                        ix2.setRegVal(mbr.getRegVal());
                        break;
                    default:
                }
            }
            else if ("STX".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int I = res[1];
                int R = res[2];
                int IX = res[3];
                int Addr = res[4];
                setRegVal("MAR", intToBinArrSh(Integer.toBinaryString(EA))); 
                switch (IX) {
                    case 1:
                        setRegVal("MBR", ix1.getRegVal());
                        setMemVal(EA, mbr.getRegVal()); 
                        break;
                    case 2:
                        setRegVal("MBR", ix2.getRegVal());
                        setMemVal(EA, mbr.getRegVal()); 
                        break;
                    case 3:
                        setRegVal("MBR", ix3.getRegVal());
                        setMemVal(EA, mbr.getRegVal()); 
                        break;
                    default:
                }
            }
            else if ("HLT".equals(inst)) {
                int[] message = new int[]{1};
                hlt.setRegVal(message);
            }
            else if("IN".equals(inst)){
                int[] res = comp(binInst);
                int R = res[2];
                String input = JOptionPane.showInputDialog("Please enter a character: ");
                if(input != null && input.length() > 0) {
                    int val = (int)input.charAt(0);
                    int[] bin = intToBinArr(Integer.toBinaryString(val));
                    switch(R) {
                        case 0:
                            gpr0.setRegVal(bin);
                            break;
                        case 1:
                            gpr1.setRegVal(bin);
                            break;
                        case 2:
                            gpr2.setRegVal(bin);
                            break;
                        default:
                            gpr3.setRegVal(bin);
                    }
                }
            }
            else if ("OUT".equals(inst)) {
                int[] res = comp(binInst);
                int R = res[2];
                int[] bin;
                switch (R) {
                    case 0:
                        bin = gpr0.getRegVal();
                        break;
                    case 1:
                        bin = gpr1.getRegVal();
                        break;
                    case 2:
                        bin = gpr2.getRegVal();
                        break;
                    default:
                        bin = gpr3.getRegVal();
                }
                int val = Handler.BTI(bin);
                System.out.print((char) val);
            }
            else if ("ADD".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int R = res[2];
                int[] memVal = getMemVal(EA);
                int meVal = Handler.BTI(memVal);
                switch (R) {
                    case 0:
                        int RV0 = Handler.BTI(gpr0.getRegVal());
                        gpr0.setRegVal(intToBinArr(Integer.toBinaryString(RV0 + meVal))); 
                        break;
                    case 1:
                        int RV1 = Handler.BTI(gpr1.getRegVal());
                        gpr1.setRegVal(intToBinArr(Integer.toBinaryString(RV1 + meVal))); 
                        break;
                    case 2:
                        int RV2 = Handler.BTI(gpr2.getRegVal());
                        gpr2.setRegVal(intToBinArr(Integer.toBinaryString(RV2 + meVal))); 
                        break;
                    default:
                        int RV3 = Handler.BTI(gpr3.getRegVal());
                        gpr3.setRegVal(intToBinArr(Integer.toBinaryString(RV3 + meVal))); 
                }
            }
            else if ("SUB".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int R = res[2];
                int[] memVal = getMemVal(EA);
                int meVal = Handler.BTI(memVal);
                switch (R) {
                    case 0:
                        int RV0 = Handler.BTI(gpr0.getRegVal());
                        gpr0.setRegVal(intToBinArr(Integer.toBinaryString(RV0 - meVal))); 
                        break;
                    case 1:
                        int RV1 = Handler.BTI(gpr1.getRegVal());
                        gpr1.setRegVal(intToBinArr(Integer.toBinaryString(RV1 - meVal))); 
                        break;
                    case 2:
                        int RV2 = Handler.BTI(gpr2.getRegVal());
                        gpr2.setRegVal(intToBinArr(Integer.toBinaryString(RV2 - meVal))); 
                        break;
                    default:
                        int RV3 = Handler.BTI(gpr3.getRegVal());
                        gpr3.setRegVal(intToBinArr(Integer.toBinaryString(RV3 - meVal))); 
                }
            }
            else if ("MUL".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int R = res[2];
                int[] memVal = getMemVal(EA);
                int meVal = Handler.BTI(memVal);
                switch (R) {
                    case 0:
                        int RV0 = Handler.BTI(gpr0.getRegVal());
                        gpr0.setRegVal(intToBinArr(Integer.toBinaryString(RV0 * meVal)));
                        break;
                    case 1:
                        int RV1 = Handler.BTI(gpr1.getRegVal());
                        gpr1.setRegVal(intToBinArr(Integer.toBinaryString(RV1 * meVal)));
                        break;
                    case 2:
                        int RV2 = Handler.BTI(gpr2.getRegVal());
                        gpr2.setRegVal(intToBinArr(Integer.toBinaryString(RV2 * meVal)));
                        break;
                    default:
                        int RV3 = Handler.BTI(gpr3.getRegVal());
                        gpr3.setRegVal(intToBinArr(Integer.toBinaryString(RV3 * meVal)));
                }
            }
            else if ("DIV".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int R = res[2];
                int[] memVal = getMemVal(EA);
                int meVal = Handler.BTI(memVal);
                if (meVal == 0) {
                    int[] fault = {0, 0, 1, 0};
                    mfr.setRegVal(fault);
                    int[] mes = {1};
                    hlt.setRegVal(mes);
                    return;
                }
                switch (R) {
                    case 0:
                        int RV0 = Handler.BTI(gpr0.getRegVal());
                        gpr0.setRegVal(intToBinArr(Integer.toBinaryString(RV0 / meVal)));
                        break;
                    case 1:
                        int RV1 = Handler.BTI(gpr1.getRegVal());
                        gpr1.setRegVal(intToBinArr(Integer.toBinaryString(RV1 / meVal)));
                        break;
                    case 2:
                        int RV2 = Handler.BTI(gpr2.getRegVal());
                        gpr2.setRegVal(intToBinArr(Integer.toBinaryString(RV2 / meVal)));
                        break;
                    default:
                        int RV3 = Handler.BTI(gpr3.getRegVal());
                        gpr3.setRegVal(intToBinArr(Integer.toBinaryString(RV3 / meVal)));
                }
            }
            else if ("AND".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int R = res[2];
                int IX = res[3];
                int[] reg2 = new int[16];
                switch (IX){
                    case 1: reg2 = ix1.getRegVal();
                        break;
                    case 2: reg2 = ix2.getRegVal();
                        break;
                    default:
                        reg2 = ix3.getRegVal();
                }
                switch (R) {
                    case 0:
                        int[] RV0 = gpr0.getRegVal();
                        int[] NV0 = new int[16];
                        for (int i = 0; i < 16; i++) {
                            NV0[i] = RV0[i] & reg2[i];
                        }
                        gpr0.setRegVal(NV0);
                        break;
                    case 1:
                        int[] RV1 = gpr1.getRegVal();
                        int[] NV1 = new int[16];
                        for (int i = 0; i < 16; i++) {
                            NV1[i] = RV1[i] & reg2[i];
                        }
                        gpr1.setRegVal(NV1);
                        break;
                    case 2:
                        int[] RV2 = gpr2.getRegVal();
                        int[] NV2 = new int[16];
                        for (int i = 0; i < 16; i++) {
                            NV2[i] = RV2[i] & reg2[i];
                        }
                        gpr2.setRegVal(NV2);
                        break;
                    default:
                        int[] RV3 = gpr3.getRegVal();
                        int[] NV3 = new int[16];
                        for (int i = 0; i < 16; i++) {
                            NV3[i] = RV3[i] & reg2[i];
                        }
                        gpr3.setRegVal(NV3);
                }
            }
            else if ("ORR".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int R = res[2];
                int IX = res[3];
                int[] reg2 = new int[16];
                switch (IX){
                    case 0: reg2 = ix1.getRegVal();
                    break;
                    case 1: reg2 = ix2.getRegVal();
                        break;
                    default:
                        reg2 = ix3.getRegVal();
                }
                switch (R) {
                    case 0:
                        int[] RV0 = gpr0.getRegVal();
                        int[] NV0 = new int[16];
                        for (int i = 0; i < 16; i++) {
                            NV0[i] = RV0[i] | reg2[i];
                        }
                        gpr0.setRegVal(NV0);
                        break;
                    case 1:
                        int[] RV1 = gpr1.getRegVal();
                        int[] NV1 = new int[16];
                        for (int i = 0; i < 16; i++) {
                            NV1[i] = RV1[i] | reg2[i];
                        }
                        gpr1.setRegVal(NV1);
                        break;
                    case 2:
                        int[] RV2 = gpr2.getRegVal();
                        int[] NV2 = new int[16];
                        for (int i = 0; i < 16; i++) {
                            NV2[i] = RV2[i] | reg2[i];
                        }
                        gpr2.setRegVal(NV2);
                        break;
                    default:
                        int[] RV3 = gpr3.getRegVal();
                        int[] NV3 = new int[16];
                        for (int i = 0; i < 16; i++) {
                            NV3[i] = RV3[i] | reg2[i];
                        }
                        gpr3.setRegVal(NV3);
                }
            }
            else if ("NOT".equals(inst)) {
                int[] res = comp(binInst);
                int R = res[2];
                switch (R) {
                    case 0:
                        int[] RV0 = gpr0.getRegVal();
                        int[] NV0 = new int[16];
                        for (int i = 0; i < 16; i++) {
                            NV0[i] = RV0[i] == 0 ? 1 : 0;
                        }
                        gpr0.setRegVal(NV0);
                        break;
                    case 1:
                        int[] RV1 = gpr1.getRegVal();
                        int[] NV1 = new int[16];
                        for (int i = 0; i < 16; i++) {
                            NV1[i] = RV1[i] == 0 ? 1 : 0;
                        }
                        gpr1.setRegVal(NV1);
                        break;
                    case 2:
                        int[] RV2 = gpr2.getRegVal();
                        int[] NV2 = new int[16];
                        for (int i = 0; i < 16; i++) {
                            NV2[i] = RV2[i] == 0 ? 1 : 0;
                        }
                        gpr2.setRegVal(NV2);
                        break;
                    default:
                        int[] RV3 = gpr3.getRegVal();
                        int[] NV3 = new int[16];
                        for (int i = 0; i < 16; i++) {
                            NV3[i] = RV3[i] == 0 ? 1 : 0;
                        }
                        gpr3.setRegVal(NV3);
                }
            }
            else if ("JZ".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int R = res[2];
                int[] regVal;
                switch (R) {
                    case 0:
                        regVal = gpr0.getRegVal();
                        break;
                    case 1:
                        regVal = gpr1.getRegVal();
                        break;
                    case 2:
                        regVal = gpr2.getRegVal();
                        break;
                    default:
                        regVal = gpr3.getRegVal();
                }
                if (Handler.BTI(regVal) == 0) {
                    setRegVal("PC", intToBinArrSh(Integer.toBinaryString(EA)));
                }
            }
            else if ("JNE".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int R = res[2];
                int[] regVal;
                switch (R) {
                    case 0:
                        regVal = gpr0.getRegVal();
                        break;
                    case 1:
                        regVal = gpr1.getRegVal();
                        break;
                    case 2:
                        regVal = gpr2.getRegVal();
                        break;
                    default:
                        regVal = gpr3.getRegVal();
                }
                if (Handler.BTI(regVal) != 0) {
                    setRegVal("PC", intToBinArrSh(Integer.toBinaryString(EA)));
                }
            }
            else if ("JCC".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int R = res[2];
                int[] c = cc.getRegVal();
                if (c[R] == 1) {
                    setRegVal("PC", intToBinArrSh(Integer.toBinaryString(EA)));
                }
            }
            else if ("AMR".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int R = res[2];
                int[] memVal = getMemVal(EA);
                int meVal = Handler.BTI(memVal);
                switch (R) {
                    case 0:
                        int RV0 = Handler.BTI(gpr0.getRegVal());
                        gpr0.setRegVal(intToBinArr(Integer.toBinaryString(RV0 + meVal)));
                        break;
                    case 1:
                        int RV1 = Handler.BTI(gpr1.getRegVal());
                        gpr1.setRegVal(intToBinArr(Integer.toBinaryString(RV1 + meVal)));
                        break;
                    case 2:
                        int RV2 = Handler.BTI(gpr2.getRegVal());
                        gpr2.setRegVal(intToBinArr(Integer.toBinaryString(RV2 + meVal)));
                        break;
                    default:
                        int RV3 = Handler.BTI(gpr3.getRegVal());
                        gpr3.setRegVal(intToBinArr(Integer.toBinaryString(RV3 + meVal)));
                }
            }
            else if ("SMR".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int R = res[2];
                int[] memVal = getMemVal(EA);
                int meVal = Handler.BTI(memVal);
                switch (R) {
                    case 0:
                        int RV0 = Handler.BTI(gpr0.getRegVal());
                        gpr0.setRegVal(intToBinArr(Integer.toBinaryString(RV0 - meVal)));
                        break;
                    case 1:
                        int RV1 = Handler.BTI(gpr1.getRegVal());
                        gpr1.setRegVal(intToBinArr(Integer.toBinaryString(RV1 - meVal)));
                        break;
                    case 2:
                        int RV2 = Handler.BTI(gpr2.getRegVal());
                        gpr2.setRegVal(intToBinArr(Integer.toBinaryString(RV2 - meVal)));
                        break;
                    default:
                        int RV3 = Handler.BTI(gpr3.getRegVal());
                        gpr3.setRegVal(intToBinArr(Integer.toBinaryString(RV3 - meVal)));
                }
            }
            else if ("AIR".equals(inst)) {
                int[] res = comp(binInst);
                int ied = res[4];
                int R = res[2];
                switch (R) {
                    case 0:
                        int RV0 = Handler.BTI(gpr0.getRegVal());
                        gpr0.setRegVal(intToBinArr(Integer.toBinaryString(RV0 + ied)));
                        break;
                    case 1:
                        int RV1 = Handler.BTI(gpr1.getRegVal());
                        gpr1.setRegVal(intToBinArr(Integer.toBinaryString(RV1 + ied)));
                        break;
                    case 2:
                        int RV2 = Handler.BTI(gpr2.getRegVal());
                        gpr2.setRegVal(intToBinArr(Integer.toBinaryString(RV2 + ied)));
                        break;
                    default:
                        int RV3 = Handler.BTI(gpr3.getRegVal());
                        gpr3.setRegVal(intToBinArr(Integer.toBinaryString(RV3 + ied)));
                }
            }
            else if ("SIR".equals(inst)) {
                int[] res = comp(binInst);
                int ied = res[4];
                int R = res[2];
                switch (R) {
                    case 0:
                        int RV0 = Handler.BTI(gpr0.getRegVal());
                        gpr0.setRegVal(intToBinArr(Integer.toBinaryString(RV0 - ied)));
                        break;
                    case 1:
                        int RV1 = Handler.BTI(gpr1.getRegVal());
                        gpr1.setRegVal(intToBinArr(Integer.toBinaryString(RV1 - ied)));
                        break;
                    case 2:
                        int RV2 = Handler.BTI(gpr2.getRegVal());
                        gpr2.setRegVal(intToBinArr(Integer.toBinaryString(RV2 - ied)));
                        break;
                    default:
                        int RV3 = Handler.BTI(gpr3.getRegVal());
                        gpr3.setRegVal(intToBinArr(Integer.toBinaryString(RV3 - ied)));
                }
            }
            else if ("JMA".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                setRegVal("PC", intToBinArrSh(Integer.toBinaryString(EA)));
            }
            else if ("JSR".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int R = res[2];
                int[] curPC = getRegVal("PC");
                int nxtInstr = Handler.BTI(curPC);
                gpr3.setRegVal(intToBinArr(Integer.toBinaryString(nxtInstr)));
                setRegVal("PC", intToBinArrSh(Integer.toBinaryString(EA)));
            }
            else if ("RFS".equals(inst)) {
                int[] res = comp(binInst);
                int R = res[2];
                int[] returnAddr = gpr3.getRegVal();
                setRegVal("PC", intToBinArrSh(Integer.toBinaryString(Handler.BTI(returnAddr))));
            }
            else if ("SOB".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int R = res[2];
                int[] regVal;
                switch (R) {
                    case 0:
                        regVal = gpr0.getRegVal(); 
                        int V0 = Handler.BTI(regVal) - 1; 
                        gpr0.setRegVal(intToBinArr(Integer.toBinaryString(V0)));
                        if (V0 > 0) {
                            setRegVal("PC", intToBinArrSh(Integer.toBinaryString(EA)));
                        }
                        break;
                    case 1:
                        regVal = gpr1.getRegVal();
                        int V1 = Handler.BTI(regVal) - 1;
                        gpr1.setRegVal(intToBinArr(Integer.toBinaryString(V1)));
                        if (V1 > 0) {
                            setRegVal("PC", intToBinArrSh(Integer.toBinaryString(EA)));
                        }
                        break;
                    case 2:
                        regVal = gpr2.getRegVal();
                        int V2 = Handler.BTI(regVal) - 1;
                        gpr2.setRegVal(intToBinArr(Integer.toBinaryString(V2)));
                        if (V2 > 0) {
                            setRegVal("PC", intToBinArrSh(Integer.toBinaryString(EA)));
                        }
                        break;
                    default:
                        regVal = gpr3.getRegVal();
                        int V3 = Handler.BTI(regVal) - 1;
                        gpr3.setRegVal(intToBinArr(Integer.toBinaryString(V3)));
                        if (V3 > 0) {
                            setRegVal("PC", intToBinArrSh(Integer.toBinaryString(EA)));
                        }
                }
            }
            else if ("JGE".equals(inst)) {
                int[] res = comp(binInst);
                int EA = res[0];
                int R = res[2];
                int[] regVal;
                switch (R) {
                    case 0:
                        regVal = gpr0.getRegVal();
                        break;
                    case 1:
                        regVal = gpr1.getRegVal();
                        break;
                    case 2:
                        regVal = gpr2.getRegVal();
                        break;
                    default:
                        regVal = gpr3.getRegVal();
                }
                if (Handler.BTI(regVal) >= 0) {
                    setRegVal("PC", intToBinArrSh(Integer.toBinaryString(EA)));
                }
            }
            else if ("MLT".equals(inst)) {
                int[] res = comp(binInst);
                int rx = res[2];
                int ix = res[3];
                int[] reg2 = new int[16];
                switch (ix){
                    case 1: reg2 = ix1.getRegVal();
                        break;
                    case 2: reg2 = ix2.getRegVal();
                        break;
                    default:
                        reg2 = ix3.getRegVal();
                }
                int V1, V2;
                V2 = Handler.BTI(reg2);
                switch (rx) {
                    case 0: V1 = Handler.BTI(gpr0.getRegVal()); break;
                    case 1: V1 = Handler.BTI(gpr1.getRegVal()); break;
                    case 2: V1 = Handler.BTI(gpr2.getRegVal()); break;
                    default: V1 = Handler.BTI(gpr3.getRegVal());
                }
                int mulRes = V1 * V2;
                if (rx < 3) {
                    switch (rx) {
                        case 0:
                            gpr0.setRegVal(intToBinArr(Integer.toBinaryString(mulRes >> 16)));
                            gpr1.setRegVal(intToBinArr(Integer.toBinaryString(mulRes & 0xFFFF)));
                            break;
                        case 1:
                            gpr1.setRegVal(intToBinArr(Integer.toBinaryString(mulRes >> 16)));
                            gpr2.setRegVal(intToBinArr(Integer.toBinaryString(mulRes & 0xFFFF)));
                            break;
                        case 2:
                            gpr2.setRegVal(intToBinArr(Integer.toBinaryString(mulRes >> 16)));
                            gpr3.setRegVal(intToBinArr(Integer.toBinaryString(mulRes & 0xFFFF)));
                            break;
                    }
                }
                else {
                    int[] fault = {0, 1, 0, 0};
                    mfr.setRegVal(fault);
                }
            }
            else if ("DVD".equals(inst)) {
                int[] res = comp(binInst);
                int rx = res[2];
                int IX = res[3];
                int[] reg2 = new int[16];
                switch (IX){
                    case 1: reg2 = ix1.getRegVal();
                        break;
                    case 2: reg2 = ix2.getRegVal();
                        break;
                    default:
                        reg2 = ix3.getRegVal();
                }
                int V1, V2;
                V2 = Handler.BTI(reg2);
                switch (rx) {
                    case 0: V1 = Handler.BTI(gpr0.getRegVal()); break;
                    case 1: V1 = Handler.BTI(gpr1.getRegVal()); break;
                    case 2: V1 = Handler.BTI(gpr2.getRegVal()); break;
                    default: V1 = Handler.BTI(gpr3.getRegVal());
                }
                int mulRes = V1 / V2;
                if (rx < 3) {
                    switch (rx) {
                        case 0:
                            gpr0.setRegVal(intToBinArr(Integer.toBinaryString(mulRes >> 16)));
                            gpr1.setRegVal(intToBinArr(Integer.toBinaryString(mulRes & 0xFFFF)));
                            break;
                        case 1:
                            gpr1.setRegVal(intToBinArr(Integer.toBinaryString(mulRes >> 16)));
                            gpr2.setRegVal(intToBinArr(Integer.toBinaryString(mulRes & 0xFFFF)));
                            break;
                        case 2:
                            gpr2.setRegVal(intToBinArr(Integer.toBinaryString(mulRes >> 16)));
                            gpr3.setRegVal(intToBinArr(Integer.toBinaryString(mulRes & 0xFFFF)));
                            break;
                    }
                } else {
                    int[] fault = {0, 1, 0, 0};
                    mfr.setRegVal(fault);
                }
            }
        }
    }
    public String decOpCode(int[] binOpCode){
        String retVal;
        String opCode = Arrays.toString(binOpCode);
        opCode = opCode.replace("[", "");
        opCode = opCode.replace("]", "");
        opCode = opCode.replace(",", "");
        opCode = opCode.replace(" ", "");
        retVal = switch (opCode) {
            case "000001" -> "LDR";
            case "000010" -> "STR";
            case "000011" -> "LDA";
            case "000100" -> "AMR";
            case "000101" -> "SMR";
            case "000110" -> "AIR";
            case "000111" -> "SIR";
            case "001000" -> "JZ";
            case "001001" -> "JNE";
            case "001010" -> "JCC";
            case "001011" -> "JMA";
            case "001100" -> "JSR";
            case "001101" -> "RFS";
            case "001110" -> "SOB";
            case "001111" -> "JGE";
            case "010000" -> "MLT";
            case "010001" -> "DVD";
            case "010011" -> "AND";
            case "010100" -> "ORR";
            case "010101" -> "NOT";
            case "011000" -> "IN";
            case "011001" -> "OUT";
            case "100001" -> "LDX";
            case "100010" -> "STX";
            default -> "HLT";
        };
        return retVal;
    }
    public int[] getRegVal(String register){
        return switch (register) {
            case "PC" -> pc.getRegVal();
            case "CC" -> cc.getRegVal();
            case "IR" -> ir.getRegVal();
            case "MAR" -> mar.getRegVal();
            case "MBR" -> mbr.getRegVal();
            case "MFR" -> mfr.getRegVal();
            case "IX1" -> ix1.getRegVal();
            case "IX2" -> ix2.getRegVal();
            case "IX3" -> ix3.getRegVal();
            case "GPR0" -> gpr0.getRegVal();
            case "GPR1" -> gpr1.getRegVal();
            case "GPR2" -> gpr2.getRegVal();
            case "GPR3" -> gpr3.getRegVal();
            case null, default -> hlt.getRegVal();
        };
    }
    public void setRegVal(String register, int[] value){
        switch (register) {
            case "PC" -> {
                if (Handler.BTI(value) < 10) {
                    int[] tmp_val = {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0};
                    pc.setRegVal(tmp_val);
                } else {
                    pc.setRegVal(value);
                }
            }
            case "CC" -> cc.setRegVal(value);
            case "IR" -> ir.setRegVal(value);
            case "MAR" -> mar.setRegVal(value);
            case "MBR" -> mbr.setRegVal(value);
            case "MFR" -> mfr.setRegVal(value);
            case "IX1" -> ix1.setRegVal(value);
            case "IX2" -> ix2.setRegVal(value);
            case "IX3" -> ix3.setRegVal(value);
            case "GPR0" -> gpr0.setRegVal(value);
            case "GPR1" -> gpr1.setRegVal(value);
            case "GPR2" -> gpr2.setRegVal(value);
            case "GPR3" -> gpr3.setRegVal(value);
            case null, default -> hlt.setRegVal(value);
        }
    }
    public int[] getMemVal(int row){
        if (row < 6){
            return new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

        }else{
            return MAIN_MEM.getMemVal(row);
        }
    }
    public void setMemVal(int row, int[] value){
        if (row < 6){
            int[] fault = new int[]{0,0,0,1};
            mfr.setRegVal(fault);
            int [] message = new int[]{1};
            hlt.setRegVal(message);
        }else{
            MAIN_MEM.setMemVal(row, value);
        }
    }
    public int[] comp(int[] inst) {
        String strInst = Arrays.toString(inst).replace("[", "").replace("]", "").replace(",", "").replace(" ", "");
        int I = (strInst.charAt(10) == '0') ? 0 : 1;
        int R = calReg(strInst.substring(6, 8));
        int IX = calReg(strInst.substring(8, 10));
        int[] addressField = Arrays.copyOfRange(inst, 11, 16);
        int EA;
        if (I == 0) {
            EA = (IX == 0) ? Handler.BTI(addressField) : Handler.BTI(addressField) + getIxVal(IX);
        } else {
            EA = calIndEA(addressField, IX);
        }
        return new int[]{EA, I, R, IX, Handler.BTI(addressField)};
    }
    private int calReg(String binaryString) {
        return switch (binaryString) {
            case "00" -> 0;
            case "01" -> 1;
            case "10" -> 2;
            case "11" -> 3;
            default -> 0;
        };
    }
    private int getIxVal(int IX) {
        String IXRegister = (IX == 1) ? "IX1" : (IX == 2) ? "IX2" : "IX3";
        return Handler.BTI(getRegVal(IXRegister));
    }
    private int calIndEA(int[] addressField, int IX) {
        int tmpVar = Handler.BTI(addressField);
        if (IX != 0) {
            int ixVal = getIxVal(IX);
            tmpVar += ixVal;
        }
        int[] tmp_var = getMemVal(tmpVar);
        tmpVar = Handler.BTI(tmp_var);
        return Handler.BTI(getMemVal(tmpVar));
    }
    public void loadIPL(String path) throws FileNotFoundException, IOException {
        try (BufferedReader r = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(path))))) {
            String str;
            while ((str = r.readLine()) != null) {
                String[] k = str.split(" ");
                int row = Handler.OTI(k[0]);
                int[] rowBin = Handler.octTobinArr(k[0],12);
                setRegVal("MAR", rowBin);
                int [] val = Handler.octTobinArr(k[1],16);
                setRegVal("MBR", val);
                System.out.println("System is currently setting the memory for the given ROW "+ row);
                setMemVal(row, val);
                int[] fault = {0, 0, 0, 0};
                mfr.setRegVal(fault);
            }
        }
    }
    public void fileOpen() throws IOException {
        JFileChooser file = new JFileChooser();
        int res = file.showOpenDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            File sFile = file.getSelectedFile();
            try {
                loadIPL(sFile.getAbsolutePath());
                if(sFile.getAbsolutePath().contains("P1")){
                    filestat = true;
                }
                if(sFile.getAbsolutePath().contains("P2")){
                    filestat2 = true;
                }
                } catch (IOException ex) {
                    System.out.println("There is an error occured when loading the file. Please Try Again!!!" + ex);
            }
                int[] defPCLoc = new int[]{0,0,0,0,0,0,0,0,1,1,1,0};
                setRegVal("PC",defPCLoc);
        } else {
            System.out.println("Error in the dialog box. Please Try Again!!!");
        }
    }
    public int[] intToBinArr(String intVal){
        int[] retVal = new int[16];
        char[] ar = intVal.toCharArray();
        for (int i = 0; i < 16; i++) {
            if (i < 16 - ar.length){
                retVal[i] = 0;
            }
            else {
                retVal[i] = Character.getNumericValue(intVal.charAt(i-(16 - ar.length)));
            }
        }
        return retVal;
    }
    public int[] intToBinArrSh(String intVal){
        int[] retVal = new int[12];
        char[] ar = intVal.toCharArray();
        for (int i = 0; i < 12; i++) {
            if (i < 12 - ar.length){
                retVal[i] = 0;
            }else{
                retVal[i] = Character.getNumericValue(intVal.charAt(i-(12 - ar.length)));
            }

        }
        return retVal;
    }
}