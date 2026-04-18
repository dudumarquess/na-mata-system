# Na Mata Sys Frontend

Frontend Angular do sistema **Na Mata Sys**, focado em uso familiar com fluxo simples para:
- abrir mês
- registar receitas diárias
- registar gastos
- ver resumo do mês
- gerir gastos fixos
- ajustar configurações

## Stack

- Angular 21 (standalone)
- Angular Router
- HttpClient
- Reactive Forms
- SCSS

## Estrutura principal

- `src/app/core` - base HTTP, guardas, estado global do mês, toasts
- `src/app/shared` - componentes reutilizáveis e utilitários de erro
- `src/app/features` - páginas da aplicação
- `src/app/layout` - shell com header e navegação
- `src/app/models` - contratos TypeScript da API
- `src/app/services` - serviços de integração REST

## Configurar backend

Defina a URL base da API em:

- `src/environments/environment.ts`
- `src/environments/environment.development.ts`

Exemplo atual:

```ts
apiBaseUrl: 'http://localhost:8080'
```

## Executar localmente

```bash
cd na-mata-sys-frontend
npm install
npm start
```

Abrir no navegador:

- `http://localhost:4200`

## Build de produção

```bash
npm run build
```

## Rotas

- `/` -> redireciona para `/mes`
- `/mes`
- `/inicio`
- `/receitas`
- `/gastos`
- `/resumo`
- `/gastos-fixos`
- `/configuracoes`

As rotas (exceto `/mes`) pedem mês selecionado. Se não houver mês, o utilizador volta para seleção de mês.

## Contrato de resposta suportado

O frontend está preparado para respostas no formato:

```json
{
  "success": true,
  "message": "...",
  "data": { },
  "errors": []
}
```

ou

```json
{
  "success": false,
  "message": "...",
  "data": null,
  "errors": [
    { "field": "campo", "message": "mensagem" }
  ]
}
```

Mensagens de erro de campo são mostradas junto dos inputs e também em alertas/toasts quando necessário.
