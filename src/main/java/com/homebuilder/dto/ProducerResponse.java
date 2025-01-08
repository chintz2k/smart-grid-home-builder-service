package com.homebuilder.dto;

import com.homebuilder.entity.Producer;

/**
 * @author André Heinen
 */
public class ProducerResponse {

	private final Long id;
	private final String name;
	private final boolean active;
	private final boolean archived;
	private final double powerProduction;
	private final boolean renewable;
	private final boolean solarPower;
	private final boolean windPower;
	private final boolean hydroPower;
	private final boolean geothermalPower;
	private final boolean biomassPower;
	private final boolean coalPower;
	private final boolean naturalGasPower;
	private final boolean oilPower;
	private final boolean nuclearPower;

	public ProducerResponse(Producer producer) {
		this.id = producer.getId();
		this.name = producer.getName();
		this.active = producer.isActive();
		this.archived = producer.isArchived();
		this.powerProduction = producer.getPowerProduction();
		this.renewable = producer.isRenewable();
		this.solarPower = producer.isSolarPower();
		this.windPower = producer.isWindPower();
		this.hydroPower = producer.isHydroPower();
		this.geothermalPower = producer.isGeothermalPower();
		this.biomassPower = producer.isBiomassPower();
		this.coalPower = producer.isCoalPower();
		this.naturalGasPower = producer.isNaturalGasPower();
		this.oilPower = producer.isOilPower();
		this.nuclearPower = producer.isNuclearPower();
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean isActive() {
		return active;
	}

	public boolean isArchived() {
		return archived;
	}

	public double getPowerProduction() {
		return powerProduction;
	}

	public boolean isRenewable() {
		return renewable;
	}

	public boolean isSolarPower() {
		return solarPower;
	}

	public boolean isWindPower() {
		return windPower;
	}

	public boolean isHydroPower() {
		return hydroPower;
	}

	public boolean isGeothermalPower() {
		return geothermalPower;
	}

	public boolean isBiomassPower() {
		return biomassPower;
	}

	public boolean isCoalPower() {
		return coalPower;
	}

	public boolean isNaturalGasPower() {
		return naturalGasPower;
	}

	public boolean isOilPower() {
		return oilPower;
	}

	public boolean isNuclearPower() {
		return nuclearPower;
	}
}
