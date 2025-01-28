package com.homebuilder.entity;

import com.homebuilder.validation.ValidProducerType;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

/**
 * @author André Heinen
 */
@Entity
@ValidProducerType
public class Producer extends Device {

	@NotNull(message = "Maximum output production is required")
	@DecimalMin(value = "0.001", inclusive = true, message = "Maximum output production must be at least 0.001")
	private double powerProduction;

	private boolean renewable;

	// Erneuerbare
	private boolean solarPower;
	private boolean windPower;
	private boolean hydroPower;
	private boolean geothermalPower;
	private boolean biomassPower;

	// Fossile
	private boolean coalPower;
	private boolean naturalGasPower;
	private boolean oilPower;
	private boolean nuclearPower;

	public Producer() {

	}

	private void updateRenewableStatus() {
		renewable = solarPower || windPower || hydroPower || geothermalPower || biomassPower;
	}

	public double getPowerProduction() {
		return powerProduction;
	}

	public void setPowerProduction(double powerProduction) {
		this.powerProduction = powerProduction;
		updateRenewableStatus();
	}

	public boolean isRenewable() {
		return renewable;
	}

	public void setRenewable(boolean renewable) {
		this.renewable = renewable;
	}

	public boolean isSolarPower() {
		return solarPower;
	}

	public void setSolarPower(boolean solarPower) {
		this.solarPower = solarPower;
		updateRenewableStatus();
	}

	public boolean isWindPower() {
		return windPower;
	}

	public void setWindPower(boolean windPower) {
		this.windPower = windPower;
		updateRenewableStatus();
	}

	public boolean isHydroPower() {
		return hydroPower;
	}

	public void setHydroPower(boolean hydroPower) {
		this.hydroPower = hydroPower;
		updateRenewableStatus();
	}

	public boolean isGeothermalPower() {
		return geothermalPower;
	}

	public void setGeothermalPower(boolean geothermalPower) {
		this.geothermalPower = geothermalPower;
		updateRenewableStatus();
	}

	public boolean isBiomassPower() {
		return biomassPower;
	}

	public void setBiomassPower(boolean biomassPower) {
		this.biomassPower = biomassPower;
		updateRenewableStatus();
	}

	public boolean isCoalPower() {
		return coalPower;
	}

	public void setCoalPower(boolean coalPower) {
		this.coalPower = coalPower;
		updateRenewableStatus();
	}

	public boolean isNaturalGasPower() {
		return naturalGasPower;
	}

	public void setNaturalGasPower(boolean naturalGasPower) {
		this.naturalGasPower = naturalGasPower;
		updateRenewableStatus();
	}

	public boolean isOilPower() {
		return oilPower;
	}

	public void setOilPower(boolean oilPower) {
		this.oilPower = oilPower;
		updateRenewableStatus();
	}

	public boolean isNuclearPower() {
		return nuclearPower;
	}

	public void setNuclearPower(boolean nuclearPower) {
		this.nuclearPower = nuclearPower;
		updateRenewableStatus();
	}

	public String getPowerType() {
		if (solarPower) {
			return "Solar Power";
		} else if (windPower) {
			return "Wind Power";
		} else if (hydroPower) {
			return "Hydro Power";
		} else if (geothermalPower) {
			return "Geothermal Power";
		} else if (biomassPower) {
			return "Biomass Power";
		} else if (coalPower) {
			return "Coal Power";
		} else if (naturalGasPower) {
			return "Natural Gas Power";
		} else if (oilPower) {
			return "Oil Power";
		} else if (nuclearPower) {
			return "Nuclear Power";
		}
		return "Unknown Power Type";
	}
}
