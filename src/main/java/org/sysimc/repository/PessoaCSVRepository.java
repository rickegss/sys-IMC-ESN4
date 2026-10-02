package org.sysimc.repository;

import org.sysimc.model.Pessoa;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PessoaCSVRepository {
    private final Path arquivo;

    public PessoaCSVRepository(String caminho) {
        this.arquivo = Paths.get(caminho);
    }

    public void salvar(List<Pessoa> pessoas) throws IOException {
        List<String> linhas = new ArrayList<>();
        for (Pessoa p : pessoas) {
            linhas.add(p.getId() + "," + p.getNome() + "," + p.getAltura() + "," + p.getPeso());
        }
        Files.write(arquivo, linhas, StandardCharsets.UTF_8);
    }

    public List<Pessoa> carregar() throws IOException {
        List<Pessoa> pessoas = new ArrayList<>();
        if (!Files.exists(arquivo)) {
            return pessoas;
        }
        for (String linha : Files.readAllLines(arquivo, StandardCharsets.UTF_8)) {
            if (linha.isBlank()) {
                continue;
            }
            String[] partes = linha.split(",");
            if (partes.length != 4) {
                continue;
            }
            try {
                Pessoa p = new Pessoa(partes[1].trim(), Float.parseFloat(partes[2].trim()), Float.parseFloat(partes[3].trim()));
                p.setId(Integer.parseInt(partes[0].trim()));
                pessoas.add(p);
            } catch (NumberFormatException e) {
                continue;
            }
        }
        return pessoas;
    }
}