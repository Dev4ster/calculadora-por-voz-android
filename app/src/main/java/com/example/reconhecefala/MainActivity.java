package com.example.reconhecefala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String mensagem;
    TextView txt;
    private final int REQ_CODE_SPEECH_INPUT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = findViewById(R.id.texto);

    }

    /*
        MOSTRAR ENTRADA DE DIALOGO DO GOOGLE SPEECH
      */
    private void promptSpeechInput(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "DIGA ALGO");

        try{
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);

        }catch (ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(),
                    "Desculpe, dispositivo sem este recurso",
                    Toast.LENGTH_LONG).show();
        }

    }


    /*
        RECEBE ENTRADA DO GOOGLE SPEECH
     */

    private Boolean isNumeric(String[] dados){
       try{
           Double.parseDouble(dados[0]);
           Double.parseDouble(dados[0]);
           return true;
       }catch (NumberFormatException e){
           return false;
       }
    }

    private Double Calcular(String[] dados){
        switch(dados[1]){
            case "+" :
            case "mais":
                return Double.parseDouble(dados[0]) + Double.parseDouble(dados[2]);
            case "-" :
            case "menos":
                return Double.parseDouble(dados[0]) - Double.parseDouble(dados[2]);
            case "/" :
            case "dividido":
            case "dividido por":
                return Double.parseDouble(dados[0]) / Double.parseDouble(dados[2]);
            case "*" :
            case "vezes":
            case "x":
                return Double.parseDouble(dados[0]) * Double.parseDouble(dados[2]);
            default:
                return 0.0;

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE_SPEECH_INPUT){
            if(resultCode == RESULT_OK && null != data){
                ArrayList<String> resultado;
                resultado = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                mensagem = resultado.get(0);
                String dados[] = new String[3];
                dados = mensagem.split(" ");

                if(dados.length == 3){
                    if(isNumeric(dados)) {
                        txt.setText(dados[0]+dados[1]+dados[2]+"= " +Calcular(dados) + "");

                        return;
                    }
                }

                txt.setText("erro na expressão , exemplo diga: \n 1 mais 1 ou 2 menos 1 \n termos aceitos: \n mais,menos,vezes,dividido");
            }
        }
    }

    public void falar(View view) {
        promptSpeechInput();
    }
}
