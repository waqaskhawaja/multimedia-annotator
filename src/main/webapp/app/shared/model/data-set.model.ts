import { Moment } from 'moment';
import { IAnalysisScenario } from 'app/shared/model/analysis-scenario.model';

export interface IDataSet {
    id?: number;
    title?: string;
    date?: Moment;
    type?: string;
    contents?: any;
    identifier?: string;
    analysisScenario?: IAnalysisScenario;
}

export class DataSet implements IDataSet {
    constructor(
        public id?: number,
        public title?: string,
        public date?: Moment,
        public type?: string,
        public contents?: any,
        public identifier?: string,
        public analysisScenario?: IAnalysisScenario
    ) {}
}
