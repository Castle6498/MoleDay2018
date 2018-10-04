/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team6498.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.IterativeRobot;


public class Robot extends IterativeRobot {
	public NetworkTableInstance table;
	
	public NetworkTable nTable;
	
	public Robot() {
		
		nTable = NetworkTableInstance.getDefault().getTable("GRIP/myContoursReport/centerX");
	}
	
	@Override
	public void robotInit() {
		CameraServer.getInstance().startAutomaticCapture();
		
		
		
	}

	
	@Override
	public void autonomousInit() {
		
	}
	
	NetworkTableEntry entry;
	double[] number;
	
	@Override
	public void autonomousPeriodic() {
		entry = nTable.getEntry("centerX");
		entry.getDoubleArray(number);
		System.out.println(entry.getNumberArray(Number[] {0,0}));
	}

	
	@Override
	public void teleopPeriodic() {
		
	}

	
	@Override
	public void testPeriodic() {
	}
}
