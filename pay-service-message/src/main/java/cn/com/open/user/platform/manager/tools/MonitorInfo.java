package cn.com.open.user.platform.manager.tools;

public class MonitorInfo {

	private long totalMemory;				//可使用jvm内存
	private long freeMemory;				//剩余jvm内存
	private long maxMemory;					//最大可使用jvm内存
	private String osName;					//操作系统
	private long totalMemorySize;			//总的物理内存
	private long freePhysicalMemorySize;	//剩余的物理内存
	private long usedMemory;				//已使用的物理内存
	private int totalThread;				//线程总数
	private double cpuRatio;				//CPU使用率
	private long usableSpace;				//可用磁盘空间 kb
	private long totalSpace;				//总磁盘空间 kb
	
	public long getTotalMemory() {
		return totalMemory;
	}
	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}
	public long getFreeMemory() {
		return freeMemory;
	}
	public void setFreeMemory(long freeMemory) {
		this.freeMemory = freeMemory;
	}
	public long getMaxMemory() {
		return maxMemory;
	}
	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}
	public String getOsName() {
		return osName;
	}
	public void setOsName(String osName) {
		this.osName = osName;
	}
	public long getTotalMemorySize() {
		return totalMemorySize;
	}
	public void setTotalMemorySize(long totalMemorySize) {
		this.totalMemorySize = totalMemorySize;
	}
	public long getFreePhysicalMemorySize() {
		return freePhysicalMemorySize;
	}
	public void setFreePhysicalMemorySize(long freePhysicalMemorySize) {
		this.freePhysicalMemorySize = freePhysicalMemorySize;
	}
	public long getUsedMemory() {
		return usedMemory;
	}
	public void setUsedMemory(long usedMemory) {
		this.usedMemory = usedMemory;
	}
	public int getTotalThread() {
		return totalThread;
	}
	public void setTotalThread(int totalThread) {
		this.totalThread = totalThread;
	}
	public double getCpuRatio() {
		return cpuRatio;
	}
	public void setCpuRatio(double cpuRatio) {
		this.cpuRatio = cpuRatio;
	}
	public long getUsableSpace() {
		return usableSpace;
	}
	public void setUsableSpace(long usableSpace) {
		this.usableSpace = usableSpace;
	}
	public long getTotalSpace() {
		return totalSpace;
	}
	public void setTotalSpace(long totalSpace) {
		this.totalSpace = totalSpace;
	}
	
}
