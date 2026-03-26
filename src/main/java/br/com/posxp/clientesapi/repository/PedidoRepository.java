package br.com.posxp.clientesapi.repository;

import br.com.posxp.clientesapi.model.Pedido;
import br.com.posxp.clientesapi.model.PedidoStatus;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @EntityGraph(attributePaths = {"cliente", "itens", "itens.produto"})
    @Query("select distinct p from Pedido p")
    List<Pedido> findAllWithDetalhes(Sort sort);

    @EntityGraph(attributePaths = {"cliente", "itens", "itens.produto"})
    @Query("select distinct p from Pedido p where p.id = :id")
    Optional<Pedido> findByIdWithDetalhes(Long id);

    @EntityGraph(attributePaths = {"cliente", "itens", "itens.produto"})
    @Query("select distinct p from Pedido p where p.status = :status")
    List<Pedido> findByStatusWithDetalhes(PedidoStatus status, Sort sort);

    boolean existsByClienteId(Long clienteId);
}
