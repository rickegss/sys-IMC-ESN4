package org.sysimc.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.sysimc.model.Pessoa;
import org.sysimc.repository.PessoaCSVRepository;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class MainController {
    @FXML
    public TextField txtNome;

    @FXML
    public TextField txtAltura;

    @FXML
    public TextField txtPeso;

    @FXML
    public Label lbIMC;

    @FXML
    public Label lbClassificacaoIMC;

    @FXML
    private TableView<Pessoa> tabelaPessoas;

    @FXML
    private TableColumn<Pessoa, Integer> colId;

    @FXML
    private TableColumn<Pessoa, String> colNome;

    @FXML
    private TableColumn<Pessoa, Float> colAltura;

    @FXML
    private TableColumn<Pessoa, Float> colPeso;

    @FXML
    private TableColumn<Pessoa, String> colIMC;

    private final ObservableList<Pessoa> pessoas = FXCollections.observableArrayList();
    private final PessoaCSVRepository repository = new PessoaCSVRepository("dados_pessoas.txt");
    private final DecimalFormat df = new DecimalFormat("#0.00");
    private int proximoId = 1;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colAltura.setCellValueFactory(new PropertyValueFactory<>("altura"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colIMC.setCellValueFactory(c -> new SimpleStringProperty(df.format(c.getValue().getImc())));
        tabelaPessoas.setItems(pessoas);
    }

    @FXML
    protected void onCalcularIMCClick() {
        Pessoa pessoa = lerPessoaDosCampos();
        if (pessoa == null) {
            return;
        }
        exibirIMC(pessoa);
    }

    @FXML
    protected void onSalvarClick() {
        Pessoa pessoa = lerPessoaDosCampos();
        if (pessoa == null) {
            return;
        }
        pessoa.setId(proximoId++);
        pessoas.add(pessoa);
        exibirIMC(pessoa);
        try {
            repository.salvar(pessoas);
        } catch (IOException e) {
            mostrarErro("Não foi possível salvar o arquivo: " + e.getMessage());
        }
    }

    @FXML
    protected void onCarregarDadosClick() {
        try {
            List<Pessoa> carregadas = repository.carregar();
            pessoas.setAll(carregadas);
            proximoId = carregadas.stream().mapToInt(Pessoa::getId).max().orElse(0) + 1;
        } catch (IOException e) {
            mostrarErro("Não foi possível ler o arquivo: " + e.getMessage());
        }
    }

    private Pessoa lerPessoaDosCampos() {
        String nome = txtNome.getText().trim();
        if (nome.isEmpty()) {
            mostrarErro("Informe o nome.");
            return null;
        }
        if (nome.contains(",")) {
            mostrarErro("O nome não pode conter vírgula.");
            return null;
        }
        try {
            float altura = Float.parseFloat(txtAltura.getText().trim().replace(',', '.'));
            float peso = Float.parseFloat(txtPeso.getText().trim().replace(',', '.'));
            if (altura <= 0 || peso <= 0) {
                mostrarErro("Altura e peso devem ser maiores que zero.");
                return null;
            }
            return new Pessoa(nome, altura, peso);
        } catch (NumberFormatException e) {
            mostrarErro("Altura e peso devem ser números válidos.");
            return null;
        }
    }

    private void exibirIMC(Pessoa pessoa) {
        lbIMC.setText(df.format(pessoa.calcularIMC()));
        lbClassificacaoIMC.setText(pessoa.classificacaoIMC());
    }

    private void mostrarErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}