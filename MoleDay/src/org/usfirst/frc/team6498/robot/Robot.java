
package org.usfirst.frc.team6498.robot;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team6498.control.PIDControlHelper;
import org.usfirst.frc.team6498.control.PidGyroAngle;
import org.usfirst.frc.team6498.control.PidGyroDisplacement;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.vision.VisionThread;


public class Robot extends IterativeRobot {
	private static final int IMG_WIDTH = 320;
	private static final int IMG_HEIGHT = 240;
	
	private VisionThread visionThread;
	
	public Compressor compressor;
	
	private class VisionOutput{
		double centerX=0.0;
		boolean targetLocated=false;
	}
	//private double centerX = 0.0;
	private VisionOutput v;
	
	
	private final Object imgLock = new Object();
	
	public DifferentialDrive base;
	
	
	Joystick j;
     
     
     
     public PIDControlHelper turnController;            
     static double kPturnAngle = 0.04;
     static double kIturnAngle = 0.0;//0.00002;//0.00014;//26;1
     static double kDturnAngle = 0;//0.003;//5;
     static double kFturnAngle = 0.00;
     static double kToleranceDegreesturnAngle = 0;//0.5f;
	
	
	
    public AHRS nav;
	
	@Override
	public void robotInit() {
		v=new VisionOutput();
		j=new Joystick(0);
		
		base= new DifferentialDrive(new Spark(0),new Spark(1));
		
		nav = new AHRS(SPI.Port.kMXP);
		
		compressor=new Compressor();
      	
      	turnController = new PIDControlHelper(kPturnAngle, kIturnAngle, kDturnAngle, kFturnAngle, kToleranceDegreesturnAngle, 0, new PidGyroAngle(nav), -720,720);
  		
		
		
		  UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		    camera.setResolution(IMG_WIDTH, IMG_HEIGHT);
		    
		    visionThread = new VisionThread(camera, new GripPipeline(), pipeline -> {
		        if (!pipeline.filterContoursOutput().isEmpty()) {
		            Rect r = Imgproc.boundingRect(pipeline.filterContoursOutput().get(0));
		            synchronized (imgLock) {
		                v.centerX = r.x + (r.width / 2);
		                v.targetLocated=true;
		               // System.out.println(centerX);
		            }
		        }else {
		        	 synchronized (imgLock) {
		        		 v.centerX = 0;
			                v.targetLocated=false;
			            }
		        	  
		        }
		    });
		    visionThread.start();
		
		    
		
	}

	
	@Override
	public void autonomousInit() {
		compressor.start();
		System.out.println("initialized");
		turnController.enable();
	}
	
	
	
	@Override
	public void autonomousPeriodic() {
		//System.out.println("running");
		 double centerX;
		 boolean located;
		    synchronized (imgLock) {
		        centerX = this.v.centerX;
		        located=this.v.targetLocated;
		    }
		    
		   double turn = centerX - (IMG_WIDTH/2); /// 2);
		    
		   //System.out.println("centerX "+turn);
		   double result;
		   
		    double setPoint = nav.getAngle()+(30*turn)/160;
		    if(located) {
		    turnController.set(setPoint);
		   
		     result = turnController.result;
		     System.out.println("result "+result+" angle "+setPoint + " coordinate "+turn+" error: "+turnController.turnController.getError());
		    }else {
		     result=0;
		    }
		    //System.out.println("SetPoint "+setPoint);
		   
		   
		   
		    
		    base.tankDrive(result, -result);
		
		    
		    
	}

	public void autonomousDisable() {
		turnController.disable();
	}
	
	
	public void teleopInit() {
		compressor.start();
	}
	
	@Override
	public void teleopPeriodic() {
		base.arcadeDrive(j.getY(), -j.getX());
	}

	
	@Override
	public void testPeriodic() {
		base.tankDrive(j.getY(), j.getY());
		System.out.println(j.getY());
	}




}

