package com.opentext.interview.messagingsys.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;

public class ProcessInfoUtils {

	public static long showProcessInfo() {
		RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
		String jvmName = runtimeBean.getName();

//		System.out.print("JVM Name = " + jvmName + " \t");

		long pid = Long.valueOf(jvmName.split("@")[0]);
//		System.out.print("JVM PID  = " + pid + " \t");

//		ThreadMXBean bean = ManagementFactory.getThreadMXBean();
//		int peakThreadCount = bean.getPeakThreadCount();

//		System.out.println("Peak Thread Count = " + peakThreadCount);

		return pid;

	}
}