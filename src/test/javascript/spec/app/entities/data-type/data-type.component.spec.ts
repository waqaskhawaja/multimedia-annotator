/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MultimediaAnnotatorTestModule } from '../../../test.module';
import { DataTypeComponent } from 'app/entities/data-type/data-type.component';
import { DataTypeService } from 'app/entities/data-type/data-type.service';
import { DataType } from 'app/shared/model/data-type.model';

describe('Component Tests', () => {
    describe('DataType Management Component', () => {
        let comp: DataTypeComponent;
        let fixture: ComponentFixture<DataTypeComponent>;
        let service: DataTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MultimediaAnnotatorTestModule],
                declarations: [DataTypeComponent],
                providers: []
            })
                .overrideTemplate(DataTypeComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(DataTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DataTypeService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new DataType(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.dataTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
