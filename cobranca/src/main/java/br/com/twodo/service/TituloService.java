package br.com.twodo.service;

import br.com.twodo.model.StatusTitulo;
import br.com.twodo.model.Titulo;
import br.com.twodo.repository.TituloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by JoaoLuizSK on 06/07/17.
 */
@Service
public class TituloService {

    @Autowired
    private TituloRepository tituloRepository;

    public void salvar(Titulo titulo){
       try{
            tituloRepository.save(titulo);

        }  catch (DataIntegrityViolationException e){
            throw new IllegalArgumentException("Formato de data inválido!!");
        }
    }

    public List<Titulo> listarTodos(){
        return tituloRepository.findAll();
    }

    public void excluir(Long codigo){
        tituloRepository.delete(codigo);
    }

    public String receber(Long codigo){
        Titulo titulo = tituloRepository.findOne(codigo);
        titulo.setStatus(StatusTitulo.RECEBIDO);
        tituloRepository.save(titulo);
        return titulo.getStatus().getDescricao();
    }

    public List<Titulo> listarTituloPorDescricao(String descricao){
        return tituloRepository.findByDescricaoContaining(descricao);
    }

}
