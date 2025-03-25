package com.homebuilder.dto;

import com.homebuilder.entity.Producer;
import com.homebuilder.validation.ValidProducerType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * @author André Heinen
 */
@ValidProducerType
public class ProducerRequest {

	private Long id = null;

	@NotBlank(message = "name is required")
	private String name;

	private Long ownerId = null;

	private boolean active = false;

	private boolean archived = false;

	@NotNull(message = "Maximum output production is required")
	@DecimalMin(value = "0.001", inclusive = true, message = "Maximum output production must be at least 0.001")
	private double powerProduction;

	private Long roomId = null;

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

	public ProducerRequest() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isArchived() {
		return archived;
	}

	public void setArchived(boolean archived) {
		this.archived = archived;
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

	public Long getRoomId() {
		return roomId;
	}

	public void setRoomId(Long roomId) {
		this.roomId = roomId;
	}

	/**
	 * Converts this ProducerRequest object into a Producer entity object.
	 * Only for new Producers. If you want to update a Producer, refer directly to the fields of the ProducerRequest.
	 *
	 * @return a new Producer entity, only for creating new Producers.
	 */
	public Producer toEntity() {
		Producer producer = new Producer();
		producer.setName(name);
		producer.setPowerProduction(powerProduction);
		producer.setSolarPower(solarPower);
		producer.setWindPower(windPower);
		producer.setHydroPower(hydroPower);
		producer.setGeothermalPower(geothermalPower);
		producer.setBiomassPower(biomassPower);
		producer.setCoalPower(coalPower);
		producer.setNaturalGasPower(naturalGasPower);
		producer.setOilPower(oilPower);
		producer.setNuclearPower(nuclearPower);
		updateRenewableStatus();
		return producer;
	}
}
