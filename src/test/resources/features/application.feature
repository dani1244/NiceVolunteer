# language: pt
Funcionalidade: Gestão de Candidaturas a Oportunidades
  Como um voluntário
  Quero candidatar-me a oportunidades
  Para poder participar em atividades de voluntariado

  Cenário: Voluntário candidata-se com sucesso a uma oportunidade aberta
    Dado que existe um voluntário com email "john@ua.pt"
    E existe uma oportunidade aberta "Beach Cleanup"
    Quando o voluntário se candidata à oportunidade
    Então a candidatura deve ser criada com sucesso
    E o estado da candidatura deve ser "PENDING"

  Cenário: Voluntário não pode candidatar-se duas vezes à mesma oportunidade
    Dado que existe um voluntário com email "john@ua.pt"
    E existe uma oportunidade aberta "Beach Cleanup"
    E o voluntário já se candidatou à oportunidade
    Quando o voluntário tenta candidatar-se novamente
    Então deve receber um erro de candidatura inválida

  Cenário: Promotor aceita candidatura de voluntário
    Dado que existe um voluntário com email "john@ua.pt"
    E existe uma oportunidade aberta "Beach Cleanup"
    E o voluntário candidatou-se à oportunidade
    Quando o promotor aceita a candidatura
    Então o estado da candidatura deve ser "ACCEPTED"
    E a data de resposta deve estar preenchida

  Cenário: Promotor rejeita candidatura de voluntário
    Dado que existe um voluntário com email "john@ua.pt"
    E existe uma oportunidade aberta "Beach Cleanup"
    E o voluntário candidatou-se à oportunidade
    Quando o promotor rejeita a candidatura
    Então o estado da candidatura deve ser "REJECTED"
    E a data de resposta deve estar preenchida

  Cenário: Voluntário completa atividade e recebe pontos
    Dado que existe um voluntário com email "john@ua.pt"
    E existe uma oportunidade aberta "Beach Cleanup" com 15 pontos
    E o voluntário candidatou-se à oportunidade
    E a candidatura foi aceite pelo promotor
    Quando a atividade é completada
    Então o estado da candidatura deve ser "COMPLETED"
    E o voluntário deve ter 15 pontos

  Cenário: Voluntário consulta as suas candidaturas
    Dado que existe um voluntário com email "john@ua.pt"
    E existem 3 oportunidades abertas
    E o voluntário candidatou-se a 2 oportunidades
    Quando o voluntário consulta as suas candidaturas
    Então deve ver 2 candidaturas na lista
