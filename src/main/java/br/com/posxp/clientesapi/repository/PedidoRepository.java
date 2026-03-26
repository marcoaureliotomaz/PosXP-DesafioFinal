package br.com.posxp.clientesapi.repository;

import br.com.posxp.clientesapi.model.Pedido;
import br.com.posxp.clientesapi.model.PedidoStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @EntityGraph(attributePaths = {"cliente", "itens", "itens.produto"})
    @Query(value = "select distinct p from Pedido p",
            countQuery = "select count(distinct p) from Pedido p")
    Page<Pedido> findAllWithDetalhes(Pageable pageable);

    @EntityGraph(attributePaths = {"cliente", "itens", "itens.produto"})
    @Query("select distinct p from Pedido p where p.id = :id")
    Optional<Pedido> findByIdWithDetalhes(Long id);

    @EntityGraph(attributePaths = {"cliente", "itens", "itens.produto"})
    @Query(value = "select distinct p from Pedido p where p.status = :status",
            countQuery = "select count(distinct p) from Pedido p where p.status = :status")
    Page<Pedido> findByStatusWithDetalhes(PedidoStatus status, Pageable pageable);

    boolean existsByClienteId(Long clienteId);
}
