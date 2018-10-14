package org.usfirst.frc.team6498.control;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class PidGyroAngle implements PIDSource {
	
	public AHRS nav;
	public PidGyroAngle(AHRS nav) {
		this.nav=nav;
	}
	
	

	
	
	
	@Override
	public void setPIDSourceType(PIDSourceType pidSource) {
		// TODO Auto-generated method stub

	}

	@Override
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return PIDSourceType.kDisplacement;
	}

	@Override
	public double pidGet() {
		return nav.getAngle();
	}

}
