package br.com.posxp.clientesapi.repository;

import br.com.posxp.clientesapi.model.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByNomeContainingIgnoreCase(String nome);
}

