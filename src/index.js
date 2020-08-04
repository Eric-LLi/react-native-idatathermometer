import { NativeModules } from 'react-native';

const { IdataThermometer } = NativeModules;


export const iDataEvents = {
	KEY_EVENTS: 'keyevents',
    TEMPERATURE_EVENTS: 'temperatureevents'
    STATUS_EVENTS: 'statusevents'
};

const events = {};

const eventEmitter = new NativeEventEmitter(IdataThermometer);

IdataThermometer.on = (event, handler) => {
	const eventListener = eventEmitter.addListener(event, handler);

	events[event] = eventListener;
};

IdataThermometer.off = (event, handler) => {
	if (events.hasOwnProperty(event)) {
		const eventListener = events[event];

		eventListener.remove();

		delete events[event];
	}
};

export default IdataThermometer;
