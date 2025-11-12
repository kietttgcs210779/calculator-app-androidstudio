package com.example.calculatorappjava;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView expressionTextView;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        expressionTextView = findViewById(R.id.expressionTextView);
        resultTextView = findViewById(R.id.resultTextView);

        View.OnClickListener textAppendListener = v -> {
            Button button = (Button) v;
            resultTextView.append(button.getText().toString());
        };

        findViewById(R.id.button_0).setOnClickListener(textAppendListener);
        findViewById(R.id.button_1).setOnClickListener(textAppendListener);
        findViewById(R.id.button_2).setOnClickListener(textAppendListener);
        findViewById(R.id.button_3).setOnClickListener(textAppendListener);
        findViewById(R.id.button_4).setOnClickListener(textAppendListener);
        findViewById(R.id.button_5).setOnClickListener(textAppendListener);
        findViewById(R.id.button_6).setOnClickListener(textAppendListener);
        findViewById(R.id.button_7).setOnClickListener(textAppendListener);
        findViewById(R.id.button_8).setOnClickListener(textAppendListener);
        findViewById(R.id.button_9).setOnClickListener(textAppendListener);
        findViewById(R.id.button_dot).setOnClickListener(textAppendListener);
        findViewById(R.id.button_add).setOnClickListener(textAppendListener);
        findViewById(R.id.button_subtract).setOnClickListener(textAppendListener);
        findViewById(R.id.button_multiply).setOnClickListener(textAppendListener);
        findViewById(R.id.button_divide).setOnClickListener(textAppendListener);
        findViewById(R.id.button_open_paren).setOnClickListener(textAppendListener);
        findViewById(R.id.button_close_paren).setOnClickListener(textAppendListener);
        findViewById(R.id.button_clear).setOnClickListener(v -> {
            String currentText = resultTextView.getText().toString();
            if (!currentText.isEmpty()) {
                resultTextView.setText(currentText.substring(0, currentText.length() - 1));
            }
        });

        findViewById(R.id.button_all_clear).setOnClickListener(v -> {
            resultTextView.setText("");
            expressionTextView.setText("");
        });

        findViewById(R.id.button_equals).setOnClickListener(v -> {
            try {
                String expression = resultTextView.getText().toString();
                expressionTextView.setText(expression);
                double result = evaluateExpression(expression);

                if (result % 1 == 0) {
                    resultTextView.setText(String.valueOf((long) result));
                } else {
                    resultTextView.setText(String.valueOf(result));
                }

            } catch (Exception e) {
                resultTextView.setText("Mark Error");
            }
        });
    }

    private double evaluateExpression(String expression) {
        return new Object() {
            int position = -1, character;
            void nextCharacter() {
                character = (++position < expression.length()) ? expression.charAt(position) : -1;
            }

            boolean eat(int characterToEat) {
                while (character == ' ') nextCharacter();
                if (character == characterToEat) {
                    nextCharacter();
                    return true;
                }
                return false;
            }

            double parse() {
                nextCharacter();
                double x = parseExpression();
                if (position < expression.length()) {
                    throw new RuntimeException("Unexpected: " + (char)character);
                };
                return x;
            }

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if (eat('+')) x += parseTerm();
                    else if (eat('-')) x -= parseTerm();
                    else return x;
                }
            }

            double parseTerm() {
                double x = parsePrimary();
                for (;;) {
                    if (eat('*')) x *= parsePrimary();
                    else if (eat('/')) x /= parsePrimary();
                    else return x;
                }
            }

            double parsePrimary() {
                if (eat('+')) return parsePrimary();
                if (eat('-')) return -parsePrimary();
                double x;
                int startPosition = this.position;
                if (eat('(')) {
                    x = parseExpression();
                    eat(')');
                } else if ((character >= '0' && character <= '9') || character == '.') {
                    while ((character >= '0' && character <= '9') || character == '.') nextCharacter();
                    x = Double.parseDouble(expression.substring(startPosition, this.position));
                } else {
                    throw new RuntimeException("Unexpected: " + (char)character);
                }
                return x;
            }
        }.parse();
    }
}
