# react-native-idata-thermometer

## Getting started

`$ npm install react-native-idata-thermometer --save`

### Mostly automatic installation

`$ react-native link react-native-idata-thermometer`

### MainActivity.java
    import com.idatathermometer.IdataThermometerModule;

    @Override  // <--- Add this method if you want to react to keyDown
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getRepeatCount() == 0) {
            IdataThermometerModule instance = IdataThermometerModule.getInstance();
			if (instance != null) {
				instance.onKeyDownEvent(keyCode, event);
			}
		}
		super.onKeyDown(keyCode, event);
		return true;
	}

	@Override  // <--- Add this method if you want to react to keyUp
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (event.getRepeatCount() == 0) {
            IdataThermometerModule instance = IdataThermometerModule.getInstance();
			if (instance != null) {
				instance.onKeyUpEvent(keyCode, event);
			}
		}
		super.onKeyUp(keyCode, event);
		return true;
	}

## Usage
```javascript
import IdataThermometer, {iDataEvents} from 'react-native-idata-thermometer';

IdataThermometer.on(iDataEvents.STATUS_EVENTS, ({status, error}) => {
    if(status){
        //SUCCESS connect device
    } else {
        //FAIL connect device
        console.log(error);
    }
})


IdataThermometer.on(iDataEvents.KEY_EVENTS, ({key}) => {
    if(key === 'up'){
        //Click key
    } else if(key === 'down'){
        //release key
    }
})


IdataThermometer.on(iDataEvents.TEMPERATURE_EVENTS, ({temp, code, error}) => {
    //If successfully read temp

    if(temp){
        console.log("temp: " + temp);
    }else{
        console.log("Error code: " + code);
        console.log("Error message: " + error);
    }
})

//First establish connection to device
IdataThermometer.init();

//Disconnect
IdataThermometer.disconnect();

// TODO: What to do with the module?
IdataThermometer;
```
