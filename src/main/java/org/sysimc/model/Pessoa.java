package org.sysimc.model;

public class Pessoa {
    private int id;
    private String nome;
    private float altura;
    private float peso;

    public Pessoa(String nome, float altura, float peso) {
        this.nome = nome;
        this.altura = altura;
        this.peso = peso;
    }

    public Pessoa() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getImc() {
        return calcularIMC();
    }

    @Override
    public String toString() {
        return "Pessoa{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", altura=" + altura +
                ", peso=" + peso +
                '}';
    }

    public float calcularIMC() {
        return this.peso / (this.altura * this.altura);
    }

    public String classificacaoIMC() {
        float imc = calcularIMC();

        if (imc < 18.5) {
            return "Abaixo do Peso";
        } else if (imc < 24.9) {
            return "Peso Normal";
        } else if (imc < 29.9) {
            return "Sobrepeso";
        } else if (imc < 34.9) {
            return "Obesidade Grau I";
        } else if (imc < 39.9) {
            return "Obesidade Grau II";
        } else {
            return "Obesidade Grau III";
        }
    }
}