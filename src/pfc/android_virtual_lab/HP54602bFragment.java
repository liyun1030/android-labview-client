package pfc.android_virtual_lab;

import pfc.android_virtual_lab.util.Constants;
import pfc.android_virtual_lab.util.TcpClientBidirectComm;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ToggleButton;

public class HP54602bFragment extends RoboFragment{
	
	private static final String TAG = "HP54602bFragment";
	private HP54602b hp54602b;
	private boolean status;
	private Context applicationContext, activityContext;
	private View rootView;
	
	@InjectView(R.id.hp54602b_ch1_range) EditText ch1Range;
	@InjectView(R.id.hp54602b_ch2_range) EditText ch2Range;
	@InjectView(R.id.hp54602b_ch1_position) EditText ch1Position;
	@InjectView(R.id.hp54602b_ch2_position) EditText ch2Position;
	@InjectView(R.id.hpt54602b_trigger_lvl) EditText triggerLvl;
	@InjectView(R.id.hp54602b_time_range) EditText timeRange;
	@InjectView(R.id.hp54602b_time_delay) EditText timeDelay;
	@InjectView(R.id.hp54602b_ch1_button) ToggleButton ch1Enabler;
	@InjectView(R.id.hp54602b_ch2_button) ToggleButton ch2Enabler;
	@InjectView(R.id.hp54602b_autoset) ToggleButton autoset;
	@InjectView(R.id.hp54602b_config_button) Button configButton;
	@InjectView(R.id.hp54602b_ch1_function) Spinner ch1Function;
	@InjectView(R.id.hp54602b_ch2_function) Spinner ch2Function;
	@InjectView(R.id.hp54602b_ch1_coupling) Spinner ch1Coupling;
	@InjectView(R.id.hp54602b_ch2_coupling) Spinner ch2Coupling;
	@InjectView(R.id.hp54602b_ch1_probe) Spinner ch1Probe;
	@InjectView(R.id.hp54602b_ch2_probe) Spinner ch2Probe;
	@InjectView(R.id.hp54602b_trigger_src) Spinner triggerSource;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState){
		
		rootView = inflater.inflate(R.layout.fragment_hp54602b,
				container, false);		
		// Getting arguments
		status = getArguments().getBoolean(Constants.HP54602B_STATUS);
		if(status)
			hp54602b = new HP54602b();
		applicationContext = getActivity().getApplicationContext();
		activityContext = getActivity();
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		changeDeviceState(status);		
		// Let's populate spinners
		populateSpinners(rootView);
		configButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {				
				Log.d(TAG, "Do it! Button has been pressed");
				readFields();
				hp54602b.setFrame();
				Log.d(TAG, "Frame -> " + hp54602b.getFrame());
				new HP54602bComm(applicationContext, activityContext, rootView).execute(hp54602b.getFrame(),
						String.valueOf(Constants.HP54602B_QUERY));
			}			
		});
	}
	
	private void populateSpinners(View rootView){
		ArrayAdapter<CharSequence> functionAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.oscilloscope_functions, android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> couplingAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.oscilloscope_coupling, android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> probeAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.oscilloscope_probe, android.R.layout.simple_spinner_item);
		ArrayAdapter<CharSequence> triggerSourceAdapter = ArrayAdapter.createFromResource(getActivity(),
				R.array.oscilloscope_trigger_src, android.R.layout.simple_spinner_item);
		
		// Specify the layout to use when the list of choices appears		
		functionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		couplingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		probeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		triggerSourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// Apply the adapter to the spinner
		ch1Function.setAdapter(functionAdapter);
		ch2Function.setAdapter(functionAdapter);
		ch1Coupling.setAdapter(couplingAdapter);
		ch2Coupling.setAdapter(couplingAdapter);
		ch1Probe.setAdapter(probeAdapter);
		ch2Probe.setAdapter(probeAdapter);
		triggerSource.setAdapter(triggerSourceAdapter);
	}
	
	private void changeDeviceState(boolean State){
		ch1Function.setEnabled(State);
		ch2Function.setEnabled(State);
		ch1Coupling.setEnabled(State);
		ch2Coupling.setEnabled(State);
		ch1Probe.setEnabled(State);
		ch2Probe.setEnabled(State);
		triggerSource.setEnabled(State);
		
		ch1Range.setEnabled(State);
		ch1Position.setEnabled(State);
		ch2Range.setEnabled(State);
		ch2Position.setEnabled(State);
		triggerLvl.setEnabled(State);
		timeRange.setEnabled(State);
		timeDelay.setEnabled(State);
		ch1Enabler.setEnabled(State);
		ch2Enabler.setEnabled(State);
		autoset.setEnabled(State);
		configButton.setEnabled(State);
	}
	
	private void readFields(){
		hp54602b.setCh1(ch1Enabler.isChecked());
		hp54602b.setCh2(ch2Enabler.isChecked());
		hp54602b.setCh1Function(ch1Function.getSelectedItemPosition());
		hp54602b.setCh2Function(ch2Function.getSelectedItemPosition());
		hp54602b.setCh1Coupling(ch1Coupling.getSelectedItemPosition());
		hp54602b.setCh2Coupling(ch2Coupling.getSelectedItemPosition());
		hp54602b.setCh1Probe(ch1Probe.getSelectedItemPosition());
		hp54602b.setCh2Probe(ch2Probe.getSelectedItemPosition());
		hp54602b.setTriggerSource(triggerSource.getSelectedItemPosition());
		hp54602b.setCh1Range(Float.valueOf(ch1Range.getText().toString()));
		hp54602b.setCh2Range(Float.valueOf(ch2Range.getText().toString()));
		hp54602b.setCh1Pos(Float.valueOf(ch1Position.getText().toString()));
		hp54602b.setCh2Pos(Float.valueOf(ch2Position.getText().toString()));
		hp54602b.setTriggerLevel(Float.valueOf(triggerLvl.getText().toString()));
		hp54602b.setTimeDelay(Float.valueOf(timeDelay.getText().toString()));
		hp54602b.setTimeRange(Float.valueOf(timeRange.getText().toString()));
		hp54602b.setAutoset(autoset.isChecked());
	}
}
