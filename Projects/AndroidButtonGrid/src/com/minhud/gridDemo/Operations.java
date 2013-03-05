package com.minhud.gridDemo;
public class Operations
{
	private static final byte OFF = 0;
	private static final byte ON = 1;
	private static final byte COLOR = 2;
	private static final byte BRIGHTNESS = 3;
	private static final byte BLINK = 4;
	private static final byte BLINK_COLOR = 5;
	private static final byte BLINK_BRIGHTNESS = 6;
	private static final byte GET_SENSOR = 7;
	private static final byte PUSH_HIGH = 8;
	private static final byte PUSH_LOW = 9;
	private static final byte PUSH_ANALOG = 10;
	private static final byte GET_DIGITAL = 11;
	private static final byte GET_ANALOG = 12;
	private static final byte FIVE_MINUTE_TIMER_DEMO = 13;
	
	private static byte[] buildOperation(byte code, byte ... operands)
	{
		byte[] array = new byte[1 + operands.length];
		
		array[0] = code;
		for(int i = 0; i < operands.length; i++)
		{
			array[i + 1] = operands[i];
		}
		
		return array;
	}
	
	public static byte[] turnPeripheralOff(byte peripheralId)
	{
		return buildOperation(OFF, peripheralId);
	}
	
	public static byte[] turnPeripheralOn(byte peripheralId)
	{
		return buildOperation(ON, peripheralId);
	}
	
	public static byte[] setPeripheralColor(byte peripheralId, byte red, byte green, byte blue)
	{
		return buildOperation(COLOR, peripheralId, red, green, blue);
	}
	
	public static byte[] setPeripheralBrightness(byte peripheralId, byte brightness)
	{
		return buildOperation(BRIGHTNESS, peripheralId, brightness);
	}
	
	public static byte[] setPeripheralBlink(byte peripheralId, short millisecondsOn, short millisecondsOff)
	{
		return buildOperation(BLINK, peripheralId, (byte)((millisecondsOn >> 8) & 0xff), (byte)(millisecondsOn & 0xff), (byte)((millisecondsOff >> 8) & 0xff), (byte)(millisecondsOff & 0xff));
	}
	
	public static byte[] setPeripheralBlinkColor(byte peripheralId, byte red, byte green, byte blue)
	{
		return buildOperation(BLINK_COLOR, peripheralId, red, green, blue);
	}
	
	public static byte[] setPeripheralBlinkBrightness(byte peripheralId, byte brightness)
	{
		return buildOperation(BLINK_BRIGHTNESS, peripheralId, brightness);
	}
	
	public static byte[] getSensorData(byte peripheralId)
	{
		return buildOperation(GET_SENSOR, peripheralId);
	}
	
	public static byte[] pushPinHigh(byte pinId)
	{
		return buildOperation(PUSH_HIGH, pinId);
	}
	
	public static byte[] pushPinLow(byte pinId)
	{
		return buildOperation(PUSH_LOW, pinId);
	}
	
	public static byte[] pushPinAnalog(byte pinId, byte value)
	{
		return buildOperation(PUSH_ANALOG, value);
	}
	
	public static byte[] getPinDigital(byte pinId)
	{
		return buildOperation(GET_DIGITAL, pinId);
	}
	
	public static byte[] getPinAnalog(byte pinId)
	{
		return buildOperation(GET_ANALOG, pinId);
	}
	
	public static byte[] fiveMinuteTimerDemo(byte peripheralId)
	{
		return buildOperation(FIVE_MINUTE_TIMER_DEMO, peripheralId);
	}
}
