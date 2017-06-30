package br.com.twodo.controller;

import br.com.twodo.model.StatusTitulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.twodo.model.Titulo;
import br.com.twodo.repository.TituloRepository;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/titulos")
public class TituloController {
	
	@Autowired
	private TituloRepository tituloRepository;

	@RequestMapping("/novo")
	public ModelAndView novo(){
	    ModelAndView mv = new ModelAndView("CadastroTitulo");
	    mv.addObject(new Titulo());
	    mv.addObject("todosStatusTitulo", StatusTitulo.values());
		return mv;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView salvar(@Validated Titulo titulo, Errors errors){

        ModelAndView mav = new ModelAndView("CadastroTitulo");
        if(errors.hasErrors()){
            return mav;
        }

        tituloRepository.save(titulo);

        mav.addObject("mensagem", "Título salvo com sucesso!");
		return mav;
	}

	@RequestMapping
    public ModelAndView pesquisar(){
        ModelAndView mav = new ModelAndView("PesquisaTitulos");
	    List<Titulo> todos = tituloRepository.findAll();
	    mav.addObject("titulos",todos);

	    return mav;
    }

	@ModelAttribute(name = "todosStatusTitulo")
    public List<StatusTitulo> todosStatusTitulo(){
	    return Arrays.asList(StatusTitulo.values());
    }
	
}
