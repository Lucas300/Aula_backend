package com.exemplo.cadastroalunos.repository;

import com.exemplo.cadastroalunos.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {
}