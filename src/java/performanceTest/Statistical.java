package performanceTest;

import ZLYUtils.DoubleOperationUtils;

import java.io.IOException;
import java.util.*;

/**
 * 统计类
 * 用于汇总性能数据
 */
public class Statistical {
    private List<Double> cpu;
    private List<Integer> memoryVSS;//虚拟内存
    private List<Integer> memoryRSS;//物理内存
    private double cpuMax;
    private double cpuAvg;
    private int memoryVSSMax;
    private double memoryVSSAvg;
    private int memoryRSSMax;
    private double memoryRSSAvg;
    private int cpuListNum;
    private int memoryListVSSNum;
    private int memoryListRSSNum;
    private String appPackage;
    private double cpuSum;
    private double memoryVSSSum;
    private double memoryRSSSum;

    public Statistical(String appPackage) throws IOException {
        this.cpu = new ArrayList<>();
        this.memoryVSS = new ArrayList<>();
        this.memoryRSS = new ArrayList<>();
        this.appPackage = appPackage;
        this.cpuMax = 0.0;
        this.cpuAvg = 0.0;
        this.memoryVSSMax = 0;
        this.memoryVSSAvg = 0.0;
        this.memoryRSSMax = 0;
        this.memoryRSSAvg = 0.0;
        this.cpuListNum = 0;
        this.memoryListVSSNum = 0;
        this.memoryListRSSNum = 0;
        this.cpuSum = 0.0;
        this.memoryVSSSum = 0.0;
        this.memoryRSSSum = 0.0;

    }






    public synchronized void addCpu(double d) {
        this.cpu.add(d);
    }

    public synchronized void addMemory(int memoryVSS, int memoryRSS) {
        this.memoryVSS.add(memoryVSS);
        this.memoryRSS.add(memoryRSS);
    }


    public String toString() {
//        不准，多进程的没有打印
        synchronized (this) {
            for (double d : this.cpu) {
                if (d > this.cpuMax) this.cpuMax = d;
                if (d != 0.0) {
                    this.cpuSum = DoubleOperationUtils.add(this.cpuSum, d);
                    this.cpuListNum++;
                }
            }
            this.cpu.clear();
            if (this.cpuListNum != 0) {
                this.cpuAvg = DoubleOperationUtils.div(this.cpuSum, this.cpuListNum, 2);
            }
            //处理虚拟内存
            for (int i : this.memoryVSS) {
                if (i > this.memoryVSSMax) this.memoryVSSMax = i;
                this.memoryVSSSum = DoubleOperationUtils.add(this.memoryVSSSum, i * 1.0);
                this.memoryListVSSNum++;
            }
            this.memoryVSS.clear();
            if (this.memoryListVSSNum != 0) {
                this.memoryVSSAvg = DoubleOperationUtils.div(this.memoryVSSSum, this.memoryListVSSNum, 2);
            }
            for (int i : this.memoryRSS) {
                if (i > this.memoryRSSMax) this.memoryRSSMax = i;
                this.memoryRSSSum += i * 1.0;
                this.memoryListRSSNum++;
            }
            this.memoryRSS.clear();
            if (this.memoryListRSSNum != 0.0) {
                this.memoryRSSAvg = DoubleOperationUtils.div(this.memoryRSSSum, this.memoryListRSSNum, 2);
            }
        }
        return "[cpu平均:" + cpuAvg + " cpu最大:" + cpuMax + "] [VSS内存平均:" + this.memoryVSSAvg +
                " VSS内存最大:" + this.memoryVSSMax + "] [RSS内存平均:" + this.memoryRSSAvg +
                " RSS内存最大:" + this.memoryRSSMax + "]";
    }

    private void memory() {

    }

    public String getAppPackage() {
        return appPackage;
    }



}
