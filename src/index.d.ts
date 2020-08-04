export declare function init(): void;

export declare function on(
	event: iDataEvents,
	callback: (event: KeyEventsTypes | StatusEventsTypes | TemperatureEventsTypes) => void
): void;
export declare function off(
	event: iDataEvents,
	callback: (event: KeyEventsTypes | StatusEventsTypes | TemperatureEventsTypes) => void
): void;

export declare function readTemp(): void;

export enum iDataEvents {
	KEY_EVENTS = 'keyevents',
	TEMPERATURE_EVENTS = 'temperatureevents',
	STATUS_EVENTS = 'statusevents',
}

export type KeyEventsTypes = {
	key: 'down' | 'up';
};

export type StatusEventsTypes = {
	status: boolean;
	error?: string;
};

export type TemperatureEventsTypes = {
	temp: string | null;
	error?: string;
	code?: number;
};
