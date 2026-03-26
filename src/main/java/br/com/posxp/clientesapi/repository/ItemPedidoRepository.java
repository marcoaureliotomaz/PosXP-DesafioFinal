package br.com.posxp.clientesapi.repository;

import br.com.posxp.clientesapi.model.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

    boolean existsByProdutoId(Long produtoId);
}
