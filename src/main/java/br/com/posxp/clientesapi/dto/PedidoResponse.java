package br.com.posxp.clientesapi.dto;

import br.com.posxp.clientesapi.model.PedidoStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@EqualsAndHashCode(callSuper = true)
@Relation(itemRelation = "pedido", collectionRelation = "pedidos")
public class PedidoResponse extends RepresentationModel<PedidoResponse> {

    private final Long id;
    private final Long clienteId;
    private final String clienteNome;
    private final PedidoStatus status;
    private final BigDecimal total;
    private final LocalDateTime dataCriacao;
    private final List<PedidoItemResponse> itens;

    public PedidoResponse(
            Long id,
            Long clienteId,
            String clienteNome,
            PedidoStatus status,
            BigDecimal total,
            LocalDateTime dataCriacao,
            List<PedidoItemResponse> itens
    ) {
        this.id = id;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.status = status;
        this.total = total;
        this.dataCriacao = dataCriacao;
        this.itens = itens;
    }
}
