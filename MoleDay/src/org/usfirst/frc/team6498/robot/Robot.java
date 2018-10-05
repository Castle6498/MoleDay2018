
package org.usfirst.frc.team6498.robot;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.vision.VisionThread;


public class Robot extends IterativeRobot {
	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 240;
	
	private VisionThread visionThread;
	private double centerX = 0.0;
	
	private final Object imgLock = new Object();
	
	public DifferentialDrive base;
	
	@Override
	public void robotInit() {
		  UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		    camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
		    
		    visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
		        if (!pipeline.filterContoursOutput().isEmpty()) {
		            Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
		            synchronized (imgLock) {
		                centerX = r.x + (r.width / 2);
		                System.out.println(centerX);
		            }
		        }
		    });
		    visionThread.start();
		
		    base= new DifferentialDrive(new Spark(0),new Spark(1));
		
	}

	
	@Override
	public void autonomousInit() {
		System.out.println("initialized");
	}
	
	
	
	@Override
	public void autonomousPeriodic() {
		//System.out.println("running");
		 double centerX;
		    synchronized (imgLock) {
		        centerX = this.centerX;
		    }
		    
		    double turn = centerX - (IMG_WIDTH/2); /// 2);
		    double output = turn*.008;
		    System.out.println("output "+output);
		    System.out.println("turn " +turn);
		    base.arcadeDrive(0, output);
		
	}

	
	@Override
	public void teleopPeriodic() {
		
	}

	
	@Override
	public void testPeriodic() {
	}




}

