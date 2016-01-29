package com.example.eric.ageconverter;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;


public class MainActivity extends Activity implements AdapterView.OnItemSelectedListener {

  private HashMap<String, Double> humanToAnimal = new HashMap<>();
  private Spinner fromSpinner, toSpinner;
  private EditText fromYear, fromMonth;
  private TextView toYear, toMonth;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    fromSpinner = (Spinner) findViewById(R.id.animal_from_spinner);
    toSpinner = (Spinner) findViewById(R.id.animal_to_spinner);
    fromYear = (EditText) findViewById(R.id.edit_age_year);
    fromMonth = (EditText) findViewById(R.id.edit_age_month);
    toYear = (TextView) findViewById(R.id.year_result_label);
    toMonth = (TextView) findViewById(R.id.month_result_label);

    fromSpinner.setOnItemSelectedListener(this);
    toSpinner.setOnItemSelectedListener(this);

    getActionBar().hide();

    fromYear.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) { }
      @Override
      public void afterTextChanged(Editable s) {
        updateAge();
      }
    });

    fromMonth.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) { }
      @Override
      public void afterTextChanged(Editable s) {
        updateAge();
      }
    });

    populateHashMap();
    addItemsToSpinner();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }


  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
    int spinnerId = ((Spinner) parent).getId();
    String animal = (String) parent.getItemAtPosition(pos);

    changePic(animal, spinnerId);
    updateAge();
  }

  public void onNothingSelected(AdapterView<?> parent) {}

  // Add animals to Spinner
  public void addItemsToSpinner() {
    // Create an ArrayAdapter using the string array and a default spinner layout
    ArrayAdapter<CharSequence> fromAdapter = ArrayAdapter.createFromResource(this,
            R.array.animals_from_array, android.R.layout.simple_spinner_item);
    // Specify the layout to use when the list of choices appears
    fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // Apply the adapter to the spinner
    fromSpinner.setAdapter(fromAdapter);


    // Now for the second spinner
    ArrayAdapter<CharSequence> toAdapter = ArrayAdapter.createFromResource(this,
            R.array.animals_to_array, android.R.layout.simple_spinner_item);
    toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    toSpinner.setAdapter(toAdapter);
  }

  // Change card photo based on animal
  public void changePic(String animal, int spinnerId) {
    // converting string-array name to lowercase to find corresponding pic in /drawable
    if (animal.equals("Guinea pig")) {
      animal = "guinea_pig";
    } else {
      animal = animal.substring(0,1).toLowerCase() + animal.substring(1);
    }
    ImageView image;

    // if changing 'animals_from_spinner'
    if (spinnerId == R.id.animal_from_spinner) {
      image = (ImageView) findViewById(R.id.animal_from_photo);
    }
    // if changing 'animals_to_spinner'
    else {
      image = (ImageView) findViewById(R.id.animal_to_photo);
    }

    int imgId = getResources().getIdentifier(animal, "drawable", getPackageName());
    image.setImageResource(imgId);
  }

  public void updateAge() {
    try {
      String userEnteredYear = fromYear.getText().toString();
      String userEnteredMonth = fromMonth.getText().toString();

      if (userEnteredYear.matches("")) {
        userEnteredYear = "0";
      }
      if (userEnteredMonth.matches("")) {
        userEnteredMonth = "0";
      }

      int fromYearInt = Integer.parseInt(userEnteredYear);
      int fromMonthInt = Integer.parseInt(userEnteredMonth);

      if (fromMonthInt > 12) {
        fromMonthInt = 12;
      }

      Double rawAge = convertToAge(fromYearInt, fromMonthInt);
      String fromSelected = fromSpinner.getSelectedItem().toString();
      String toSelected = toSpinner.getSelectedItem().toString();

      Double ageInHuman = rawAge / humanToAnimal.get(fromSelected);
      Double finalAge = ageInHuman * humanToAnimal.get(toSelected);

      int finalYear = finalAge.intValue();
      int finalMonth = getMonth(finalAge);

      toYear.setText(Integer.toString(finalYear));
      toMonth.setText(Integer.toString(finalMonth));

      if (finalYear >= 71) {
        showDeadMessage();
      } else {
        hideDeadMessage();
      }

    } catch (NumberFormatException e){
      return;
    }
  }

  private void populateHashMap() {
    humanToAnimal.put("Human", 1.0);
    humanToAnimal.put("Oski", 2.0);
    humanToAnimal.put("Cat", 3.2);
    humanToAnimal.put("Chicken", 5.33);
    humanToAnimal.put("Cow", 3.64);
    humanToAnimal.put("Deer", 2.29);
    humanToAnimal.put("Dog", 3.64);
    humanToAnimal.put("Duck", 4.21);
    humanToAnimal.put("Elephant", 1.14);
    humanToAnimal.put("Fox", 5.71);
    humanToAnimal.put("Goat", 5.33);
    humanToAnimal.put("Groundhog", 5.71);
    humanToAnimal.put("Guinea pig", 10.0);
    humanToAnimal.put("Hamster", 20.0);
    humanToAnimal.put("Hippopotamus", 1.78);
    humanToAnimal.put("Horse", 2.0);
    humanToAnimal.put("Kangaroo", 8.89);
    humanToAnimal.put("Lion", 2.29);
    humanToAnimal.put("Monkey", 3.2);
    humanToAnimal.put("Mouse", 20.0);
    humanToAnimal.put("Parakeet", 4.44);
    humanToAnimal.put("Pig", 3.2);
    humanToAnimal.put("Pigeon", 7.27);
    humanToAnimal.put("Rabbit", 8.89);
    humanToAnimal.put("Rat", 26.67);
    humanToAnimal.put("Sheep", 5.33);
    humanToAnimal.put("Squirrel", 5.0);
    humanToAnimal.put("Wolf", 4.44);
  }

  public Double convertToAge(int year, int month) {
    double monthInDecimals = (double) month / 12;
    return (double) year + monthInDecimals;
  }

  public int getMonth(Double year) {
    double monthInDecimals = year - Math.floor(year);
    return (int) Math.round(monthInDecimals * 12);
  }

  public void showDeadMessage() {
    TextView t = (TextView) findViewById(R.id.deadMessage);
    t.setVisibility(View.VISIBLE);
  }

  public void hideDeadMessage() {
    TextView t = (TextView) findViewById(R.id.deadMessage);
    t.setVisibility(View.GONE);
  }
}
