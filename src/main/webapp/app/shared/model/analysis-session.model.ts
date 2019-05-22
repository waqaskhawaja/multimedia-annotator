import { IAnalysisScenario } from 'app/shared/model/analysis-scenario.model';

export interface IAnalysisSession {
    id?: number;
    name?: string;
    sourceFileContentType?: string;
    sourceFile?: any;
    url?: string;
    analysisScenario?: IAnalysisScenario;
}

export class AnalysisSession implements IAnalysisSession {
    constructor(
        public id?: number,
        public name?: string,
        public sourceFileContentType?: string,
        public sourceFile?: any,
        public url?: string,
        public analysisScenario?: IAnalysisScenario
    ) {}
}
