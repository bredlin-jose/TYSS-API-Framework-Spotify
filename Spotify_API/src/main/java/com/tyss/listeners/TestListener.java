package com.tyss.listeners;

import java.io.IOException;
import java.util.Properties;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.tyss.baseutil.RestAssuredBaseTest;
import com.tyss.commonutils.FileVariables;
import com.tyss.reports.Extent;
import com.tyss.reports.ExtentManager;
import com.tyss.util.RestAssuredUtil;

/**
 * Description: This class implements ITestListener and overrides methods like
 * onFinish, onTestFailure,onTestSkipped,onTestSuccess which are used in Extent
 * report
 * 
 * @author Sajal, Vikas
 */
public class TestListener implements ITestListener {

	String className;
	public static int iPassCount = 0;
	public static int iFailCount = 0;
	public static int iSkippedCount = 0;
	public static String profile = null;
	public static long totPassedTime = 0;
	public static long totFailedTime = 0;
	public static long totSkippedTime = 0;
	public static long totalTimeTaken = 0;
	public static String pass_Time = "0";
	public static String fail_Time = "0";
	public static String skip_Time = "0";
	public static String tot_Time = "0";
	public static Properties prop;
	public static String sDirPath = System.getProperty("user.dir");
	static {
		profile = System.getProperty("profile");
		profile = "JavaMail";
		System.setProperty("profile", profile);
		RestAssuredBaseTest.logger.info("Running locally " + profile);
	}

	public void onStart(ITestContext context) {

	}

	public void onTestStart(ITestResult result) {

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

	}

	/**
	 * Description:Increases the pass count and adds the pass result in Extent
	 * report
	 * 
	 * @author Sajal
	 * @param result
	 */
	public void onTestSuccess(ITestResult result) {
		iPassCount = iPassCount + 1;
		ExtentManager.getTestReport().log(Status.PASS, result.getMethod().getMethodName() + "-Test case passed");
	}

	/**
	 * Description :Increases the fail count and add fail result in Extent
	 * report,adds screenshots to the Extent report on Test case failure.
	 * 
	 * @author Sajal
	 * @param result
	 */
	public void onTestFailure(ITestResult result) {
		iFailCount = iFailCount + 1;
		ExtentManager.getTestReport().log(Status.FAIL, result.getMethod().getMethodName() + "-Test case failed");
		try {
			ExtentManager.getTestReport()
					.addScreenCaptureFromPath(RestAssuredUtil.getScreenShot(FileVariables.getScreenShotPath()));
		} catch (IOException e) {
			RestAssuredBaseTest.logger.error("Unable to attach the screenshot");
		}
	}

	/**
	 * Description :Increases the skip count and adds the skip result in Extent
	 * report.
	 * 
	 * @author Sajal
	 * @param result
	 */
	public void onTestSkipped(ITestResult result) {
		String testCaseName = result.getMethod().getMethodName();
		String description = result.getMethod().getDescription();
		ExtentTest testReport = ExtentManager.getParentReport().createNode(testCaseName, description);
		ExtentManager.setTestReport(testReport);
		iSkippedCount = iSkippedCount + 1;
		ExtentManager.getTestReport().log(Status.SKIP, result.getMethod().getMethodName() + "-Test case Skipped");
	}

	/**
	 * Description :Flushes the Extent report and sends an email of the report.
	 * 
	 * @author Sajal
	 * @paran context
	 */
	public void onFinish(ITestContext context) {
		iPassCount = context.getPassedTests().size();
		iFailCount = context.getFailedTests().size();
		iSkippedCount = context.getSkippedTests().size();
		int iTotal_Executed = iPassCount + iFailCount + iSkippedCount;
		totalTimeTaken = totPassedTime + totFailedTime + totSkippedTime;
		tot_Time = RestAssuredUtil.formatDuration(totalTimeTaken);
		Extent.getExtent().flush();
	}

}
