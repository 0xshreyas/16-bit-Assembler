import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.*;
import java.util.Arrays;
import java.util.Scanner;
public class GUI extends javax.swing.JFrame {
    opCodes OC;
    cacheMemory CM = new cacheMemory(16);
    SimpleHandler SM = new SimpleHandler();
    int[] instArr = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    int[] defPCLoc = new int[]{0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0};
    boolean checks = false;
    int count = 0;
    private static final int maxNum = 20;
    private static int[] entry = new int[maxNum];
    public GUI() {
        components();
        OC = new opCodes();
        int[] tempVal = {0,0,0,0,0,0,0,0,1,0,1,0};
        OC.setRegVal("PC",tempVal);
        new Timer(delay, main).start();
    }
    int tarAdd = 2047;
    int NBA = 1000;
    int CNA = 2000;
    int MDA = 2001;
    void cal() {
        for (int i = 0; i < 20; i++) {
            OC.setRegVal("MBR", SM.intTobinArr(entry[i]));
            OC.setMemVal(NBA + i, (OC.getRegVal("MBR")));
        }
        OC.setRegVal("GPR0", OC.getMemVal(tarAdd));
        OC.setRegVal("GPR1", SM.intTobinArr(0xFFFF));
        OC.setRegVal("GPR3", SM.intTobinArr(0));
        for (int i = 0; i < 20; i++) {
            int curAdd = NBA + i;
            OC.setRegVal("GPR2", OC.getMemVal(curAdd));
            int diff = Math.abs(SM.BTI(OC.getRegVal("GPR0")) - SM.BTI(OC.getRegVal("GPR2")));
            OC.setRegVal("MBR", SM.intTobinArr(diff));
            if (SM.BTI(OC.getRegVal("MBR")) < SM.BTI(OC.getRegVal("GPR1"))) {
                OC.setRegVal("GPR1", OC.getRegVal("MBR"));
                OC.setRegVal("GPR3", OC.getRegVal("GPR2"));
            }
        }
        OC.setMemVal(CNA, OC.getRegVal("GPR3"));
        OC.setMemVal(MDA, OC.getRegVal("GPR1"));
    }
    int closeNum(int tar, int[] nums) {
        int c = nums[0];
        int minDiff = Math.abs(tar - c);
        for (int i = 1; i < nums.length; i++) {
            int diff = Math.abs(tar - nums[i]);
            if (diff < minDiff) {
                minDiff = diff;
                c = nums[i];
            }
        }
        System.out.println(c);
        return c;
    }
    int delay = 500;
    ActionListener main = new ActionListener() {
        public void actionPerformed(ActionEvent event) {
            updReg();
            if (checks){
                String instAdd =  CM.getKey(Arrays.toString(OC.getRegVal("PC")));
                System.out.println(instAdd);
                int[] insAdd = OC.getRegVal("PC");
                int iInsAdd = SM.BTI(insAdd);
                OC.setRegVal("IR", OC.getMemVal(iInsAdd));
                if(instAdd == null){
                    cacheTextField.setText("We have a cache miss!!!");
                    System.out.println("We have a cache miss!!!");
                    CM.keyUpdate(Arrays.toString(OC.getRegVal("PC")), Arrays.toString(OC.getRegVal("IR")));
                    StringBuilder text = new StringBuilder();
                    for (String key : CM.cache.keySet()) {
                        String val = CM.cache.get(key);
                        text.append("MAR: ").append(key).append(", MBR: ").append(val).append("; ");
                    }
                    CacheValueTextField.setText(text.toString());
                    System.out.println(Arrays.toString(OC.getRegVal("PC"))+Arrays.toString(OC.getRegVal("IR")));
                }
                else{
                    cacheTextField.setText("We have a cache hit !!!");
                    System.out.println("We have a cache hit !!!");
                }
                OC.exe("single");
                if (OC.getRegVal("HLT")[0] == 1){
                    checks = false;
                    int [] message = new int[]{0};
                    OC.setRegVal("HLT",message);
                }
            }
        }
    };
    @SuppressWarnings("unchecked")
    private javax.swing.JTextField CCTextField;
    private javax.swing.JTextField GPR0TextField;
    private javax.swing.JTextField GPR1TextField;
    private javax.swing.JTextField GPR2TextField;
    private javax.swing.JTextField GPR3TextField;
    private javax.swing.JTextField IRTextField;
    private javax.swing.JTextField IXR1TextField;
    private javax.swing.JTextField IXR2TextField;
    private javax.swing.JTextField IXR3TextField;
    private javax.swing.JTextField MARTextField;
    private javax.swing.JTextField MBRTextField;
    private javax.swing.JTextField MFRTextField;
    private javax.swing.JTextField Mem0RowTextField;
    private javax.swing.JTextField Mem0ValueTextField;
    private javax.swing.JTextArea CacheValueTextField;
    private javax.swing.JTextArea KeyBoardArea;
    private javax.swing.JTextArea PrinterArea;
    private javax.swing.JTextField cacheTextField;
    private javax.swing.JTextField PCTextfField;
    private javax.swing.JPanel col1;
    private javax.swing.JPanel col2;
    private void components() {
        col1 = new javax.swing.JPanel();
        col2 = new javax.swing.JPanel();
        PCTextfField = new javax.swing.JTextField();
        MARTextField = new javax.swing.JTextField();
        MBRTextField = new javax.swing.JTextField();
        IRTextField = new javax.swing.JTextField();
        MFRTextField = new javax.swing.JTextField();
        CCTextField = new javax.swing.JTextField();
        GPR0TextField = new javax.swing.JTextField();
        GPR1TextField = new javax.swing.JTextField();
        GPR2TextField = new javax.swing.JTextField();
        GPR3TextField = new javax.swing.JTextField();
        IXR1TextField = new javax.swing.JTextField();
        IXR2TextField = new javax.swing.JTextField();
        IXR3TextField = new javax.swing.JTextField();
        Mem0ValueTextField = new javax.swing.JTextField();
        CacheValueTextField = new javax.swing.JTextArea();
        KeyBoardArea = new javax.swing.JTextArea();
        PrinterArea = new javax.swing.JTextArea();
        cacheTextField = new javax.swing.JTextField();
        JLabel KeyboardTextLabel = new JLabel("Please enter the 21 numbers: ");
        KeyboardTextLabel.setFont(new java.awt.Font("PT Serif", 1, 12));
        JLabel PrinterTextLabel = new JLabel("Current Output: ");
        PrinterTextLabel.setFont(new java.awt.Font("PT Serif", 1, 12));
        Mem0RowTextField = new javax.swing.JTextField();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        col2.setBackground(new java.awt.Color(139,0,0));
        JLabel labelGpr0 = new JLabel("[GPR 0]");
        labelGpr0.setFont(new java.awt.Font("PT Serif", 1, 13));
        JLabel labelGpr1 = new JLabel("[GPR 1]");
        labelGpr1.setFont(new java.awt.Font("PT Serif", 1, 13));
        JLabel labelGpr2 = new JLabel("[GPR 2]");
        labelGpr2.setFont(new java.awt.Font("PT Serif", 1, 13));
        JLabel labelGpr3 = new JLabel("[GPR 3]");
        labelGpr3.setFont(new java.awt.Font("PT Serif", 1, 13));
        JLabel labelIxr1 = new JLabel("[IXR 1]");
        labelIxr1.setFont(new java.awt.Font("PT Serif", 1, 13));
        JLabel labelIxr2 = new JLabel("[IXR 2]");
        labelIxr2.setFont(new java.awt.Font("PT Serif", 1, 13));
        JLabel labelIxr3 = new JLabel("[IXR 3]");
        labelIxr3.setFont(new java.awt.Font("PT Serif", 1, 13));
        GPR0TextField.setText("");
        GPR1TextField.setText("");
        GPR2TextField.setText("");
        GPR3TextField.setText("");
        IXR1TextField.setText("");
        IXR2TextField.setText("");
        IXR3TextField.setText("");
        JLabel labelPc = new JLabel("[PC]");
        labelPc.setFont(new java.awt.Font("PT Serif", 1, 13));
        JLabel labelMar = new JLabel("[MAR]");
        labelMar.setFont(new java.awt.Font("PT Serif", 1, 13));
        JLabel labelMbr = new JLabel("[MBR]");
        labelMbr.setFont(new java.awt.Font("PT Serif", 1, 13));
        JLabel labelIR = new JLabel("[IR]");
        labelIR.setFont(new java.awt.Font("PT Serif", 1, 13));
        JLabel labelMFR = new JLabel("[MFR]");
        labelMFR.setFont(new java.awt.Font("PT Serif", 1, 13));
        PCTextfField.setText("");
        MARTextField.setText("");
        MBRTextField.setText("");
        IRTextField.setText("");
        MFRTextField.setText("");
        JButton initButton = new JButton("[IPL]");
        initButton.setActionCommand("Store");
        initButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    Clicked(e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        JButton program1Button = new JButton("[Program 1]");
        program1Button.setActionCommand("Store");
        program1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    pr1ButCli(e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        JButton program2Button = new JButton("[Program 2]");
        program2Button.setActionCommand("Store");
        program2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    pr2ButCli(e);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JButton StorePlusButton = new JButton("[ST+]");
        StorePlusButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                StPlButClick(event);
            }
        });
        JButton STLoadButton = new JButton("[Store]");
        STLoadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                loadST(e);
            }
        });
        JToggleButton button_15 = new JToggleButton("15");
        button_15.setFont(new java.awt.Font("PT Serif", 0, 10));
        button_15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                fifButAcPer(event);
            }
        });
        JToggleButton button_14 = new JToggleButton("14");
        button_14.setFont(new java.awt.Font("PT Serif", 0, 10));
        button_14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                But14AcPer(event);
            }
        });
        JToggleButton button_13 = new JToggleButton("13");
        button_13.setFont(new java.awt.Font("PT Serif", 0, 10));
        button_13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                But13AcPer(event);
            }
        });
        JToggleButton button_12 = new JToggleButton("12");
        button_12.setFont(new java.awt.Font("PT Serif", 0, 10));
        button_12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                But12AcPer(event);
            }
        });
        JToggleButton button_11 = new JToggleButton("11");
        button_11.setFont(new java.awt.Font("PT Serif", 0, 10));
        button_11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                But11AcPer(event);
            }
        });
        JToggleButton button_10 = new JToggleButton("10");
        button_10.setFont(new java.awt.Font("PT Serif", 0, 10));
        button_10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                But10AcPer(event);
            }
        });
        JToggleButton button_9 = new JToggleButton("[9]");
        button_9.setFont(new java.awt.Font("PT Serif", 0, 10));
        button_9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                But9AcPer(event);
            }
        });
        JToggleButton button_8 = new JToggleButton("[8]");
        button_8.setFont(new java.awt.Font("PT Serif", 0, 10));
        button_8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                But8AcPer(event);
            }
        });
        JToggleButton button_7 = new JToggleButton("[7]");
        button_7.setFont(new java.awt.Font("PT Serif", 0, 10));
        button_7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                But7AcPer(event);
            }
        });
        JToggleButton button_6 = new JToggleButton("[6]");
        button_6.setFont(new java.awt.Font("PT Serif", 0, 10));
        button_6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                But6AcPer(event);
            }
        });
        JToggleButton button_5 = new JToggleButton("[5]");
        button_5.setFont(new java.awt.Font("PT Serif", 0, 10));
        button_5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                But5AcPer(event);
            }
        });
        JToggleButton button_4 = new JToggleButton("[4]");
        button_4.setFont(new java.awt.Font("PT Serif", 0, 10));
        button_4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                But4AcPer(event);
            }
        });
        JToggleButton button_3 = new JToggleButton("[3]");
        button_3.setFont(new java.awt.Font("PT Serif", 0, 10));
        button_3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                But3AcPer(event);
            }
        });
        JToggleButton button_2 = new JToggleButton("[2]");
        button_2.setFont(new java.awt.Font("PT Serif", 0, 10));
        button_2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                But2AcPer(event);
            }
        });
        JToggleButton button_1 = new JToggleButton("[1]");
        button_1.setFont(new java.awt.Font("PT Serif", 0, 10));
        button_1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                But1AcPer(event);
            }
        });
        JToggleButton button_0 = new JToggleButton("[0]");
        button_0.setFont(new java.awt.Font("PT Serif", 0, 10));
        button_0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                But0AcPer(event);
            }
        });

        JLabel labelOpcode = new JLabel("[OP Codes]");
        labelOpcode.setFont(new java.awt.Font("PT Serif", 1, 13));
        JLabel labelGpr = new JLabel("[GPR]");
        labelGpr.setFont(new java.awt.Font("PT Serif", 1, 13));
        JLabel ixrLabel = new JLabel("[IXR]");
        ixrLabel.setFont(new java.awt.Font("PT Serif", 1, 13));
        JLabel labelI = new JLabel("[I]");
        labelI.setFont(new java.awt.Font("PT Serif", 1, 13));
        JLabel labelAddress = new JLabel("[Address]");
        labelAddress.setFont(new java.awt.Font("PT Serif", 1, 13));
        JButton SingleStepButton = new JButton("[SS]");
        SingleStepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                SSButtonActionPerformed(event);
            }
        });
        JButton HaltButton = new JButton("[HLT]");
        HaltButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                halt(event);
            }
        });
        JButton loadPCButton = new JButton("[Load PC]");
        loadPCButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                loadPC(event);
            }
        });
        JButton loadMARButton = new JButton("[Load MAR]");
        loadMARButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                loadMAR(event);
            }
        });
        JButton loadMBRButton = new JButton("[Load MBR]");
        loadMBRButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                loadMBR(event);
            }
        });
        JButton loadGPR0Button = new JButton("[Load GPR0]");
        loadGPR0Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                loadGPR0(event);
            }
        });
        JButton loadGPR1Button = new JButton("[Load GPR1]");
        loadGPR1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                loadGPR1(event);
            }
        });
        JButton loadGPR2Button = new JButton("[Load GPR2]");
        loadGPR2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                loadGPR2(event);
            }
        });
        JButton loadGPR3Button = new JButton("[Load GPR3]");
        loadGPR3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                loadGPR3(event);
            }
        });
        JButton loadIXR1Button = new JButton("[Load IXR1]");
        loadIXR1Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                loadIXR1(event);
            }
        });
        JButton loadIXR2Button = new JButton("[Load IXR2]");
        loadIXR2Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                loadIXR2(event);
            }
        });
        JButton loadIXR3Button = new JButton("[Load IXR3]");
        loadIXR3Button.setPreferredSize(new Dimension(1500, 5000));
        loadIXR3Button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                loadIXR3(event);
            }
        });
        CCTextField.setText("jTextField12");
        JLabel labelCC = new JLabel("[CC]");
        labelCC.setFont(new java.awt.Font("PT Serif", 1, 13));
        JButton UltimateLoadButton = new JButton("[Load]");
        UltimateLoadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                SupLoadAc(e);
            }
        });
        JButton ResetButton = new JButton("[Reset]");
        ResetButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                reset(event);
            }
        });
        Mem0ValueTextField.setText("jTextField3");
        CacheValueTextField.setText("CURRENT CACHE LOADING...........");
        CacheValueTextField.setEditable(false);
        CacheValueTextField.setLineWrap(true);
        CacheValueTextField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(CacheValueTextField);
        scrollPane.setPreferredSize(new Dimension(300, 50));
        KeyBoardArea.setEditable(true);
        KeyBoardArea.setLineWrap(true);
        KeyBoardArea.setWrapStyleWord(true);
        JScrollPane scrollPaneKeyBoard = new JScrollPane(KeyBoardArea);
        scrollPaneKeyBoard.setPreferredSize(new Dimension(300, 100));
        scrollPaneKeyBoard.setMinimumSize(new Dimension(425, 100));
        scrollPaneKeyBoard.setMaximumSize(new Dimension(425, 100));
        JScrollPane scrollPaneKeyBoardPrinter = new JScrollPane(PrinterArea);
        PrinterArea.setEditable(false);
        scrollPaneKeyBoardPrinter.setPreferredSize(new Dimension(300, 100));
        scrollPaneKeyBoardPrinter.setMinimumSize(new Dimension(425, 100));
        scrollPaneKeyBoardPrinter.setMaximumSize(new Dimension(425, 100));
        final int maxNum = 20;
        JFrame frame = new JFrame("KeyBoard Input App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLayout(new BorderLayout());
        KeyBoardArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!OC.filestat) {
                    JOptionPane.showMessageDialog(frame, "P1 must be selected first. Please Try Again!!!", "File Required", JOptionPane.WARNING_MESSAGE);
                    KeyBoardArea.setText("");
                    return;
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String input = KeyBoardArea.getText().trim();
                    String[] num = input.split("\\n");
                    if (num.length > count) {
                        try {
                            int curNum = Integer.parseInt(num[count]);
                            if (count < maxNum) {
                                entry[count] = curNum;
                            }
                            count++;
                            PrinterArea.setText(String.valueOf(curNum));
                            if (count == maxNum) {
                                String nxtNumStr = JOptionPane.showInputDialog(
                                        frame,
                                        "Please enter the 21st number: ",
                                        "Required. Cannot proceed without Input",
                                        JOptionPane.QUESTION_MESSAGE
                                );
                                if (nxtNumStr != null && !nxtNumStr.isEmpty()) {
                                    int nxtNum = Integer.parseInt(nxtNumStr);
                                    KeyBoardArea.append("\n" + nxtNum);
                                    int cNum = closeNum(nxtNum, entry);
                                    cal();
                                    PrinterArea.setText("Number which is the closest to the 21st number is: " + cNum);
                                    OC.setRegVal("GPR1 ", SM.intTobinArr(cNum));
                                    OC.setRegVal("GPR2 ", new int[16]);
                                    OC.setRegVal("GPR3 ", new int[16]);
                                    OC.setRegVal("MBR ", SM.intTobinArr(cNum));
                                }
                                count = 0;
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(frame, "Error! Please enter a valid number. Try Again!!!!", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        JLabel cachelabelConsole = new JLabel("[Cache]");
        cachelabelConsole.setFont(new java.awt.Font("PT Serif", 1, 13));
        JLabel KeyBoardLabel = new JLabel("[Keyboard]");
        KeyBoardLabel.setFont(new java.awt.Font("PT Serif", 1, 13));
        JLabel PrinterLabel = new JLabel("[Printer]");
        PrinterLabel.setFont(new java.awt.Font("PT Serif", 1, 13));
        Mem0RowTextField.setText("jTextField1");
        cacheTextField.setText("{Cache Status}");
        cacheTextField.setEditable(false);
        JLabel marValueLabel = new JLabel("");
        marValueLabel.setFont(new java.awt.Font("PT Serif", 1, 13));
        JButton RunButton = new JButton("[Run]");
        RunButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent event) {
                run(event);
            }
        });
        col2.setPreferredSize(new Dimension(1800, 55000));
        JLabel headingLabel = new JLabel("CSCI_6461 Team 1"); //title
        headingLabel.setFont(new Font("PT Serif", Font.BOLD, 42));
        JLabel subjectLabel = new JLabel("Computer System Architecture Section 10");
        subjectLabel.setFont(new Font("PT Serif", Font.BOLD, 42));
        JLabel headingLabel_two = new JLabel("Team 1");
        headingLabel_two.setFont(new Font("PT Serif",Font.BOLD, 32));
        javax.swing.GroupLayout col1Layout = new javax.swing.GroupLayout(col1);
        col1.setLayout(col1Layout);
        col1Layout.setHorizontalGroup(
                col1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(col1Layout.createSequentialGroup()
                                .addGap(0)
                                .addGroup(col1Layout.createSequentialGroup()
                                        .addComponent(col2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(32, 42, Short.MAX_VALUE))
                        ));
        col1Layout.setVerticalGroup(
                col1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(col1Layout.createSequentialGroup()
                                .addComponent(col2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 101, Short.MAX_VALUE))
        );
        javax.swing.GroupLayout col2Layout = new javax.swing.GroupLayout(col2);
        col2.setLayout(col2Layout);
        col2Layout.setHorizontalGroup(
                col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(col2Layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                .addGap(1596, 1596, 1596)
                                .addComponent(headingLabel)
                                .addComponent(subjectLabel)
                                .addComponent(headingLabel_two)
                        ).addGroup(col2Layout.createSequentialGroup().addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(col2Layout.createSequentialGroup()
                                                .addGap(154, 154, 154).addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(labelOpcode, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(col2Layout.createSequentialGroup().addComponent(button_15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(button_14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(button_13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(button_12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(button_11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(button_10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGroup(col2Layout.createSequentialGroup().addGap(52, 52, 52)
                                                .addComponent(labelIR)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(IRTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                        .addGroup(col2Layout.createSequentialGroup()
                                                .addGap(52, 52, 52)
                                                .addComponent(labelPc)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                .addComponent(PCTextfField, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(loadPCButton))))
                                        .addGroup(col2Layout.createSequentialGroup()
                                                .addGap(36, 36, 36)
                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                .addComponent(labelCC)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(CCTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(labelMar, javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                        .addComponent(labelMbr, javax.swing.GroupLayout.Alignment.TRAILING))
                                                                                .addGap(5, 5, 5))
                                                                        .addComponent(labelMFR))
                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                                .addGap(1, 1, 1)
                                                                                .addComponent(MARTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addComponent(MFRTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(MBRTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(loadMARButton)
                                                        .addComponent(loadMBRButton))))
                                .addGap(87, 87, Short.MAX_VALUE)
                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, col2Layout.createSequentialGroup()
                                                .addGap(0, 280, Short.MAX_VALUE)
                                                .addComponent(initButton)
                                                .addGap(18, 18, 18)
                                                .addComponent(program1Button)
                                                .addGap(18, 18, 18)
                                                .addComponent(program2Button)
                                                .addGap(18, 18, 18)
                                                .addComponent(StorePlusButton)
                                                .addGap(18, 18, 18)
                                                .addComponent(STLoadButton)
                                                .addGap(18, 18, 18)
                                                .addComponent(UltimateLoadButton)
                                                .addGap(852, 852, 852)).addGroup(col2Layout.createSequentialGroup().addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addGroup(col2Layout.createSequentialGroup().addComponent(labelIxr1)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(IXR1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(loadIXR1Button))
                                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                                                                .addComponent(labelGpr3)
                                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                .addComponent(GPR3TextField))
                                                                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                                                                .addComponent(labelGpr2)
                                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                .addComponent(GPR2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                                .addGroup(col2Layout.createSequentialGroup()
                                                                                                        .addComponent(labelGpr0)
                                                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addComponent(GPR0TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                                                .addComponent(labelGpr1)
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addComponent(GPR1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(loadGPR0Button)
                                                                                        .addComponent(loadGPR1Button)
                                                                                        .addComponent(loadGPR2Button)
                                                                                        .addComponent(loadGPR3Button)))
                                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(labelIxr2, javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                        .addComponent(labelIxr3, javax.swing.GroupLayout.Alignment.TRAILING))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                                        .addComponent(IXR2TextField)
                                                                                        .addComponent(IXR3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(loadIXR2Button)
                                                                                        .addComponent(loadIXR3Button)))))
                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                                .addComponent(button_9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(button_8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(labelGpr, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                                .addComponent(button_7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(button_6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addComponent(ixrLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(button_5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(labelI, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(206, 206, 206)
                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                                                .addComponent(button_4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(button_3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(button_2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(button_1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                .addComponent(button_0, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                        .addComponent(labelAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGap(33, 33, 33)
                                                                                .addComponent(SingleStepButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addComponent(HaltButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(ResetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        .addComponent(RunButton)))
                                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                .addGap(223)
                                                                                                .addGroup(col2Layout.createSequentialGroup()
                                                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                                .addComponent(cachelabelConsole)
                                                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                                .addComponent(cacheTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                )
                                                                                                .addGroup(col2Layout.createSequentialGroup()
                                                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                                .addComponent(KeyBoardLabel)
                                                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                                .addComponent(KeyboardTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                )
                                                                                                .addGroup(col2Layout.createSequentialGroup()
                                                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                        .addComponent(PrinterLabel)
                                                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                                                        .addComponent(PrinterTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                                                .addGap(56, 56, 56)
                                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                                                .addComponent(scrollPane,javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                        )
                                                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                                                .addComponent(scrollPaneKeyBoard)
                                                                                        )
                                                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                                                                .addComponent(scrollPaneKeyBoardPrinter)
                                                                                        )
                                                                                        .addComponent(marValueLabel))))))))));
        col2Layout.setVerticalGroup(
                col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(col2Layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(headingLabel)
                                .addGap(16, 16, 16)
                                .addComponent(subjectLabel)
                                .addGap(16, 16, 16)
                                .addComponent(headingLabel_two))
                        .addGroup(col2Layout.createSequentialGroup()
                                .addGap(80, 80, 80)
                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(col2Layout.createSequentialGroup()
                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(marValueLabel, javax.swing.GroupLayout.Alignment.TRAILING))
                                                .addGap(220)
                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(scrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(cachelabelConsole)
                                                        .addComponent(cacheTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(scrollPaneKeyBoard)
                                                        .addComponent(KeyBoardLabel)
                                                        .addComponent(KeyboardTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(scrollPaneKeyBoardPrinter)
                                                        .addComponent(PrinterLabel)
                                                        .addComponent(PrinterTextLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGap(8, 8, 8)
                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(initButton).addComponent(program1Button).addComponent(program2Button).addComponent(StorePlusButton).addComponent(STLoadButton)
                                                        .addComponent(UltimateLoadButton)))
                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, col2Layout.createSequentialGroup()
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(labelGpr0, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                        .addComponent(GPR0TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                        .addComponent(loadGPR0Button)))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(labelGpr1)
                                                                .addComponent(GPR1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(loadGPR1Button))
                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(col2Layout.createSequentialGroup()
                                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(labelGpr2)
                                                                                .addComponent(GPR2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(loadGPR2Button))
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(labelGpr3)
                                                                                .addComponent(GPR3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(loadGPR3Button))
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(labelIxr1)
                                                                                .addComponent(IXR1TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(loadIXR1Button))
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(labelIxr2)
                                                                                .addComponent(IXR2TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(loadIXR2Button))
                                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(IXR3TextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(labelIxr3)
                                                                                .addComponent(loadIXR3Button)))
                                                                .addGroup(col2Layout.createSequentialGroup()
                                                                        .addGap(103, 103, 103)
                                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(labelCC)
                                                                                .addComponent(CCTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                        .addGap(97, 97, 97))
                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(IRTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(labelIR))
                                                .addGroup(col2Layout.createSequentialGroup()
                                                        .addGap(30, 30, 30)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(labelPc, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(loadPCButton)
                                                                .addComponent(PCTextfField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(MARTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(labelMar)
                                                                .addComponent(loadMARButton))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(MBRTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(labelMbr)
                                                                .addComponent(loadMBRButton))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                                .addComponent(MFRTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addComponent(labelMFR))
                                                        .addGap(153, 153, 153))))
                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(col2Layout.createSequentialGroup()
                                                .addGap(57, 57, 57)
                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(button_15, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button_13, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button_14, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button_12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button_10, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button_11, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button_9, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button_8, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button_7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button_6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button_5, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button_4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button_2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button_3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button_1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(button_0, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(labelOpcode)
                                                        .addComponent(labelGpr)
                                                        .addComponent(ixrLabel)
                                                        .addComponent(labelI)
                                                        .addComponent(labelAddress)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, col2Layout.createSequentialGroup()
                                                .addGap(40, 40, 40)
                                                .addGroup(col2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(SingleStepButton, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(col2Layout.createSequentialGroup()
                                                                .addComponent(ResetButton)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(RunButton)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(HaltButton)))))
                                .addGap(45, 45, 45)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(col1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(col1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 8, Short.MAX_VALUE))
        );
        pack();
    }
    private void fifButAcPer(java.awt.event.ActionEvent event) {
        if (instArr[0] == 0){
            instArr[0] = 1;
        } else{
            instArr[0] = 0;
        }
    }
    private void But13AcPer(java.awt.event.ActionEvent event) {
        if (instArr[2] == 0){
            instArr[2] = 1;
        } else{
            instArr[2] = 0;
        }
    }
    private void But12AcPer(java.awt.event.ActionEvent event) {
        if (instArr[3] == 0){
            instArr[3] = 1;
        } else{
            instArr[3] = 0;
        }
    }

    private void But10AcPer(java.awt.event.ActionEvent event) {
        if (instArr[5] == 0){
            instArr[5] = 1;
        } else{
            instArr[5] = 0;
        }
    }

    private void But9AcPer(java.awt.event.ActionEvent event) {
        if (instArr[6] == 0){
            instArr[6] = 1;
        } else{
            instArr[6] = 0;
        }
    }

    private void But5AcPer(java.awt.event.ActionEvent event) {
        if (instArr[10] == 0){
            instArr[10] = 1;
        } else{
            instArr[10] = 0;
        }
    }

    private void But7AcPer(java.awt.event.ActionEvent event) {
        if (instArr[8] == 0){
            instArr[8] = 1;
        } else{
            instArr[8] = 0;
        }
    }

    private void But1AcPer(java.awt.event.ActionEvent event) {
        if (instArr[14] == 0){
            instArr[14] = 1;
        } else{
            instArr[14] = 0;
        }
    }

    private void But2AcPer(java.awt.event.ActionEvent event) {
        if (instArr[13] == 0){
            instArr[13] = 1;
        } else{
            instArr[13] = 0;
        }
    }

    private void But4AcPer(java.awt.event.ActionEvent event) {
        if (instArr[11] == 0){
            instArr[11] = 1;
        } else{
            instArr[11] = 0;
        }
    }
    private void SSButtonActionPerformed(java.awt.event.ActionEvent event) {
        String instAdd =  CM.getKey(Arrays.toString(OC.getRegVal("PC")));
        System.out.println(instAdd);
        int[] inAddr = OC.getRegVal("PC");
        int iInAddr = SM.BTI(inAddr);
        OC.setRegVal("IR", OC.getMemVal(iInAddr));
        if(instAdd == null){
            cacheTextField.setText("[Missed Cache!!!]");
            System.out.println("Missed Cache!!!");
            CM.keyUpdate(Arrays.toString(OC.getRegVal("PC")), Arrays.toString(OC.getRegVal("IR")));
            StringBuilder text = new StringBuilder();
            for (String key : CM.cache.keySet()) {
                String val = CM.cache.get(key);
                text.append("MAR: ").append(key).append(", MBR: ").append(val).append("; ");
            }
            CacheValueTextField.setText(text.toString());
            System.out.println(Arrays.toString(OC.getRegVal("PC"))+Arrays.toString(OC.getRegVal("IR")));
        }
        else{
            cacheTextField.setText("[CACHE HIT!!!]");
            System.out.println("CACHE HIT!!!");
        }
        OC.exe("single");
    }
    private void But0AcPer(java.awt.event.ActionEvent event) {
        if (instArr[15] == 0){
            instArr[15] = 1;
        } else{
            instArr[15] = 0;
        }
    }
    private void But3AcPer(java.awt.event.ActionEvent event) {
        if (instArr[12] == 0){
            instArr[12] = 1;
        } else{
            instArr[12] = 0;
        }
    }
    private void But6AcPer(java.awt.event.ActionEvent event) {
        if (instArr[9] == 0){
            instArr[9] = 1;
        } else{
            instArr[9] = 0;
        }
    }
    private void But8AcPer(java.awt.event.ActionEvent event) {
        if (instArr[7] == 0){
            instArr[7] = 1;
        } else{
            instArr[7] = 0;
        }
    }
    private void But11AcPer(java.awt.event.ActionEvent event) {
        if (instArr[4] == 0){
            instArr[4] = 1;
        } else{
            instArr[4] = 0;
        }
    }
    private void But14AcPer(java.awt.event.ActionEvent event) {
        if (instArr[1] == 0){
            instArr[1] = 1;
        } else{
            instArr[1] = 0;
        }
    }
    private void loadReg(String registerName, int startIndex, int endIndex) {
        OC.setRegVal(registerName, Arrays.copyOfRange(instArr, startIndex, endIndex));
    }
    private void loadPC(java.awt.event.ActionEvent event) {
        loadReg("PC", 4, 16);
    }
    private void loadIR(java.awt.event.ActionEvent event) {
        loadReg("IR", 0, 16);
    }
    private void loadMAR(java.awt.event.ActionEvent event) {
        loadReg("MAR", 4, 16);
    }
    private void loadMBR(java.awt.event.ActionEvent event) {
        loadReg("MBR", 0, 16);
    }
    private void loadGPR0(java.awt.event.ActionEvent event) {
        loadReg("GPR0", 0, 16);
    }
    private void loadGPR1(java.awt.event.ActionEvent event) {
        loadReg("GPR1", 0, 16);
    }
    private void loadGPR2(java.awt.event.ActionEvent event) {
        loadReg("GPR2", 0, 16);
    }
    private void loadGPR3(java.awt.event.ActionEvent event) {
        loadReg("GPR3", 0, 16);
    }
    private void loadIXR1(java.awt.event.ActionEvent event) {
        loadReg("IX1", 0, 16);
    }
    private void loadIXR2(java.awt.event.ActionEvent event) {
        loadReg("IX2", 0, 16);
    }
    private void loadIXR3(java.awt.event.ActionEvent event) {
        loadReg("IX3", 0, 16);
    }
    private void loadST(java.awt.event.ActionEvent event) {
        int[] curMAR = OC.getRegVal("MAR");
        int tMAR = SM.BTI(curMAR);
        OC.setMemVal(tMAR, OC.getRegVal("MBR"));
    }
    private void Clicked(java.awt.event.ActionEvent event) throws IOException {
        try {
            OC.fileOpen();
        } catch (IOException ex) {
            System.out.println("ERROR!!!!!!");
        }
    }
    private void pr1ButCli(java.awt.event.ActionEvent event) throws IOException {
        try {
            OC.fileOpen();
        } catch (IOException ex) {
            System.out.println("ERROR!!!!!!");
        }
    }
    JFrame frame2 = new JFrame("My Application");
    private void pr2ButCli(java.awt.event.ActionEvent event) throws IOException {
        try {
            OC.fileOpen();
        } catch (IOException ex) {
            System.out.println("ERROR!!!!!!");
        }
        if (!OC.filestat2) {
            JOptionPane.showMessageDialog(frame2, "Please select P2 file first. P2 needs to be selected first. Please Try Again!!!", "File Required", JOptionPane.WARNING_MESSAGE);
            KeyBoardArea.setText("");
            return;
        }
        try {
            File file = new File("Testing");
            Scanner fs = new Scanner(file);
            StringBuilder pb = new StringBuilder();
            while (fs.hasNextLine()) {
                pb.append(fs.nextLine()).append(" ");
            }
            fs.close();
            String para = pb.toString().trim();
            String[] s = para.split("(?<=\\.)");
            System.out.println("File's Content: ");
            for (int i = 0; i < s.length; i++) {
                System.out.println((i + 1) + ": " + s[i].trim());
            }
            StringBuilder par = new StringBuilder();
            for (int i = 0; i < s.length; i++) {
                par.append((i + 1) + ": " + s[i].trim() + "\n");
            }
            PrinterArea.setText(para.toString());
            JFrame f1 = new JFrame("KeyBoard Input App");
            f1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f1.setSize(500, 300);
            f1.setLayout(new BorderLayout());
            String wStr = JOptionPane.showInputDialog(f1,"Please enter the word that needs to be searched: ","Input Required",JOptionPane.QUESTION_MESSAGE);
            boolean success = false;
            StringBuilder suWord = new StringBuilder();
            for (int i = 0; i < s.length; i++) {
                String[] w = s[i].trim().split("\\s+");
                for (int j = 0; j < w.length; j++) {
                    String cleared = w[j].replaceAll("[^a-zA-Z0-9]", "");
                    if (cleared.equalsIgnoreCase(wStr)) {
                        System.out.println("Word located : \"" + wStr + "\"" + "\n");
                        suWord.append("Word located : " + wStr+ "\n" );
                        System.out.println("Location : " + (i + 1) + " sentence");
                        suWord.append("location : " + (i + 1)+" sentence"+ "\n");
                        System.out.println("Location of the word in the sentence : " + (j + 1));
                        suWord.append("Location of the word in the sentence : " + (j + 1)+ "\n");
                        success = true;
                    }
                }
            }
            if (!success) {
                System.out.println("The word \"" + wStr + "\" is not present in the file.");
                suWord.append("The word \"" + wStr + "\" is not present in the file."+ "\n");
            }
            PrinterArea.setText(suWord.toString());
        } catch (FileNotFoundException e) {
            System.err.println("ERROR!!!! File not found. Please ensure that the file named Testing is present in the appropriate location. Please Try Again!!!");
        }
    }
    private void SupLoadAc(java.awt.event.ActionEvent event) {
        int[] cur_MAR = OC.getRegVal("MAR");
        int trans_MAR = SM.BTI(cur_MAR);
        OC.setRegVal("MBR", OC.getMemVal(trans_MAR));
    }
    private void StPlButClick(java.awt.event.ActionEvent event) {
        int[] curMAR = OC.getRegVal("MAR");
        int tMAR = SM.BTI(curMAR);
        OC.setMemVal(tMAR, OC.getRegVal("MBR"));
        tMAR = tMAR + 1;
        int[] new_MAR = OC.intToBinArrSh(Integer.toBinaryString(tMAR));
        OC.setRegVal("MAR", new_MAR);
    }
    private void reset(java.awt.event.ActionEvent event) {
        int[] Reset_large = new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
        int[] Reset_medium = new int[]{0,0,0,0,0,0,0,0,0,0,0,0};
        int[] Reset_small = new int[]{0,0,0,0};
        OC.setRegVal("IR", Reset_large);
        OC.setRegVal("MBR", Reset_large);
        OC.setRegVal("IX1", Reset_large);
        OC.setRegVal("IX2", Reset_large);
        OC.setRegVal("IX3", Reset_large);
        OC.setRegVal("GPR3", Reset_large);
        OC.setRegVal("GPR0", Reset_large);
        OC.setRegVal("GPR1", Reset_large);
        OC.setRegVal("GPR2", Reset_large);
        OC.setRegVal("PC", Reset_medium);
        OC.setRegVal("MAR", Reset_medium);
        OC.setRegVal("CC", Reset_small);
        OC.setRegVal("MFR", Reset_small);
        CM.cache.clear();
        CacheValueTextField.setText("");
        PrinterArea.setText("");
        KeyBoardArea.setText("");
        OC.filestat = false;
        OC.filestat2 = false;
    }
    private void halt(java.awt.event.ActionEvent event) {
        int [] message = new int[]{1};
        OC.setRegVal("HLT",message);
    }
    private void run(java.awt.event.ActionEvent event) {
        if (checks == false){
            checks = true;
        }else{
            checks = false;
        }
    }
    public void updReg() {
        upReg("PC", PCTextfField);
        upReg("CC", CCTextField);
        upReg("MAR", MARTextField);
        upMem();
        upReg("MBR", MBRTextField);
        upReg("IR", IRTextField);
        upReg("MFR", MFRTextField);
        upReg("IX1", IXR1TextField);
        upReg("IX2", IXR2TextField);
        upReg("IX3", IXR3TextField);
        upReg("GPR0", GPR0TextField);
        upReg("GPR1", GPR1TextField);
        upReg("GPR2", GPR2TextField);
        upReg("GPR3", GPR3TextField);
    }
    private void upReg(String regName, JTextField textField) {
        int[] tmpArr = OC.getRegVal(regName);
        textField.setText(fText(tmpArr));
    }
    public void upMem() {
        int[] curMAR = OC.getRegVal("MAR");
        int tMAR = SM.BTI(curMAR);
        upMem(tMAR, Mem0ValueTextField, Mem0RowTextField);
        upMem(tMAR + 1, null, null);
        upMem(tMAR + 2, null, null);
        if (tMAR > 2) {
            upMem(tMAR - 1, null, null);
            upMem(tMAR - 2, null, null);
        }
    }
    private void upMem(int add, JTextField valField, JTextField roField) {
        int[] memVal = OC.getMemVal(add);
        if (valField != null) {
            valField.setText(fText(memVal));
        }
        if (roField != null) {
            roField.setText(Integer.toString(add));
        }
    }
    public String fText(int[] arr){
        String res = Arrays.toString(arr).replaceAll("[\\[\\],]", "");
        return res;
    }
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            System.out.println(GUI.class.getName()+ ex);
        } catch (InstantiationException ex) {
            System.out.println(GUI.class.getName()+ ex);
        } catch (IllegalAccessException ex) {
            System.out.println(GUI.class.getName()+ ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            System.out.println(GUI.class.getName()+ ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GUI().setVisible(true);
            }
        });
    }
}