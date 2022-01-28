package br.com.zup.ZupNotion.config;

import br.com.zup.ZupNotion.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public List<MensagemDeErro> tratarExcecoesValidacao(MethodArgumentNotValidException exception) {
        List<MensagemDeErro> mensagemDeErros = new ArrayList<>();
        for (FieldError fieldError : exception.getFieldErrors()) {
            mensagemDeErros.add(new MensagemDeErro(fieldError.getDefaultMessage()));
        }
        return mensagemDeErros;
    }
    @ExceptionHandler(EmailJaExistenteException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public MensagemDeErro tratarExcecaoEmailJaExistenteException(EmailJaExistenteException exception){
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(DominioInvalidoException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public MensagemDeErro tratarExcecaoDominioInvalidoException(DominioInvalidoException exception){
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(SenhaInvalidaException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public MensagemDeErro tratarExcecaoSenhaInvalidaException(SenhaInvalidaException exception){
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(UsuarioNaoExisteException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public MensagemDeErro tratarExcecaoUsuarioNaoExisteException(UsuarioNaoExisteException exception){
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(TarefaNaoExisteException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MensagemDeErro tratarTarefaNaoExisteException(TarefaNaoExisteException exception){
        return new MensagemDeErro(exception.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MensagemDeErro ExcecaoDeEnumStatusInvalido(HttpMessageNotReadableException exception) {
        return new MensagemDeErro("Status inválido");
    }

}
