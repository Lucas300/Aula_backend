package com.exemplo.cadastroalunos.controller;

import com.exemplo.cadastroalunos.model.Aluno;
import com.exemplo.cadastroalunos.repository.AlunoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class AlunoController {

    private final AlunoRepository alunoRepository;

    public AlunoController(AlunoRepository alunoRepository) {
        this.alunoRepository = alunoRepository;
    }

    // Exibe a lista de alunos
    @GetMapping("/")
    public String listar(Model model) {
        model.addAttribute("alunos", alunoRepository.findAll());
        return "alunos";
    }

    // Abre o formulário de cadastro
    @GetMapping("/alunos/novo")
    public String novo(Model model) {
        model.addAttribute("aluno", new Aluno());
        return "formulario";
    }

    // Recebe os dados do formulário e cadastra o aluno
    @PostMapping("/alunos")
    public String cadastrar(
            @Valid @ModelAttribute("aluno") Aluno aluno,
            BindingResult resultado) {

        if (resultado.hasErrors()) {
            return "formulario";
        }

        // Garante que o cadastro receba um novo ID.
        aluno.setId(null);
        alunoRepository.save(aluno);

        return "redirect:/";
    }

    // Abre o formulário com os dados do aluno selecionado
    @GetMapping("/alunos/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Aluno aluno = buscarAluno(id);
        model.addAttribute("aluno", aluno);

        return "formulario";
    }

    // Recebe os dados editados e atualiza o aluno
    @PostMapping("/alunos/{id}/editar")
    public String atualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("aluno") Aluno dados,
            BindingResult resultado) {

        Aluno alunoExistente = buscarAluno(id);

        if (resultado.hasErrors()) {
            dados.setId(id);
            return "formulario";
        }

        alunoExistente.setNome(dados.getNome());
        alunoExistente.setEmail(dados.getEmail());
        alunoExistente.setCurso(dados.getCurso());
        alunoExistente.setMatricula(dados.getMatricula());

        alunoRepository.save(alunoExistente);

        return "redirect:/";
    }

    // Exclui o aluno selecionado
    @PostMapping("/alunos/{id}/excluir")
    public String excluir(@PathVariable Long id) {
        Aluno aluno = buscarAluno(id);
        alunoRepository.delete(aluno);

        return "redirect:/";
    }

    private Aluno buscarAluno(Long id) {
        return alunoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aluno não encontrado."
                ));
    }
}