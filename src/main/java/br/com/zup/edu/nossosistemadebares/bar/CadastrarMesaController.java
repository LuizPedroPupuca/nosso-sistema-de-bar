package br.com.zup.edu.nossosistemadebares.bar;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/mesas")
public class CadastrarMesaController {
    private final MesaRepository repository;

    public CadastrarMesaController(MesaRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody @Valid MesaRequest request, UriComponentsBuilder uriComponentsBuilder){

        Mesa mesa = request.paraMesa();
        mesa.mudaStatusParaOcupado();

        repository.save(mesa);

        URI location = uriComponentsBuilder.path("/mesas/{id}")
                .buildAndExpand(mesa.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<?> atualizar(@RequestBody @Valid MesaRequest request, @PathVariable Long id) {

        Mesa mesa = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nâo existe essaa reserva"));

        mesa.atualiza(request.getCapacidade());

        repository.save(mesa);

        return ResponseEntity.noContent().build();
    }

}
