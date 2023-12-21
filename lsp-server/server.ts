import { appendFileSync, rmSync } from 'fs';
import { resolve } from 'path';
import { inspect } from 'util';

import {
    createConnection,
    TextDocuments,
    ProposedFeatures,
    InitializeParams,
    InitializeResult,
    Diagnostic
} from 'vscode-languageserver/node';
import { TextDocument } from 'vscode-languageserver-textdocument';
import { DiagnosticSeverity, Position } from 'vscode-languageserver';

// LOGGING

const LOGFILE = resolve(__dirname, 'server.log');
rmSync(LOGFILE, { force: true });

function log(...args: any[]) {
    appendFileSync(LOGFILE, args
        .map(e => typeof e === 'object' ? inspect(e, { depth: null }) : e)
        .join(' ') + '\n\n'
    );
}

type DataDirection = 'in' | 'out';
const DATA_ARROWS: { [k in DataDirection]: string } = {
    in: '–(IN)→',
    out: '←(OUT)–'
};
function logDataFlow(direction: DataDirection, name: string, ...args: any[]) {
    log(`${DATA_ARROWS[direction]} ${name}`, ...args);
}

// CONNECTION AND DOCUMENTS

const connection = createConnection(ProposedFeatures.all);
const documents = new TextDocuments(TextDocument);

// Set handlers for all events
[connection, documents].forEach(x =>
    Object.entries(x)
        .filter(([k]) => k.startsWith('on'))
        .forEach(([name, fn]) => {
            if (typeof fn === 'function') {
                fn.call(x, (...args: any[]): any[] => {
                    logDataFlow('in', name, ...args);
                    return [];
                });
            }
        })
);

// Specific handler for initialize request
connection.onInitialize((params: InitializeParams): InitializeResult => {
    logDataFlow('in', 'onInitialize', params);
    const result: InitializeResult = {
        capabilities: {
            textDocumentSync: 1,
            codeActionProvider: true,
            completionProvider: {
                resolveProvider: true,
                triggerCharacters: ['.']
            }
        }
    };
    logDataFlow('out', 'InitializeResult', result);
    return result;
});

// Specific handler for content change
documents.onDidChangeContent(change => {
    sendDiagnostics(change.document);
});

function sendDiagnostics(textDocument: TextDocument): void {
    const diagnostic: Diagnostic = {
        severity: DiagnosticSeverity.Warning,
        range: {
            start: Position.create(0, 0),
            end: Position.create(0, 5)
        },
        message: `SAMPLE`,
        source: 'lsp-server-stub'
    };

    const params = { uri: textDocument.uri, diagnostics: [diagnostic] };
    logDataFlow('out', 'sendDiagnostics', params);
    connection.sendDiagnostics(params);
}

documents.listen(connection);
connection.listen();
