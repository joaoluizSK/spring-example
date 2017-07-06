package br.com.twodo.controller;

import br.com.twodo.model.StatusTitulo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.twodo.model.Titulo;
import br.com.twodo.repository.TituloRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/titulos")
public class TituloController {

    private static final String CADASTRO_VIEW = "CadastroTitulo";

	@Autowired
	private TituloRepository tituloRepository;

	@RequestMapping("/novo")
	public ModelAndView novo(){
	    ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
	    mv.addObject(new Titulo());
	    mv.addObject("todosStatusTitulo", StatusTitulo.values());
		return mv;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public String salvar(@Validated Titulo titulo, Errors errors, RedirectAttributes attributes){

        if(errors.hasErrors()){
            return CADASTRO_VIEW;
        }

        try{

            tituloRepository.save(titulo);

            attributes.addFlashAttribute("mensagem", "Título salvo com sucesso!");
            return "redirect:/titulos/novo";
        } catch (DataIntegrityViolationException e){
            errors.rejectValue("dataVencimento",null, "Formato da data inválido!");
            return CADASTRO_VIEW;
        }
	}

	@RequestMapping
    public ModelAndView pesquisar(){
        ModelAndView mav = new ModelAndView("PesquisaTitulos");
	    List<Titulo> todos = tituloRepository.findAll();
	    mav.addObject("titulos",todos);

	    return mav;
    }

    @RequestMapping("{codigo}")
    public ModelAndView edicao(@PathVariable("codigo") Titulo titulo){
        ModelAndView mv = new ModelAndView(CADASTRO_VIEW);
        mv.addObject("titulo",titulo);
        return mv;
    }

    @RequestMapping(value="{codigo}",method = RequestMethod.DELETE)
    public String excluir(@PathVariable Long codigo, RedirectAttributes attributes){
        tituloRepository.delete(codigo);
        attributes.addFlashAttribute("mensagem", "Título removido com sucesso!");
		return "redirect:/titulos";
	}

	@ModelAttribute(name = "todosStatusTitulo")
    public List<StatusTitulo> todosStatusTitulo(){
	    return Arrays.asList(StatusTitulo.values());
    }
	
}
